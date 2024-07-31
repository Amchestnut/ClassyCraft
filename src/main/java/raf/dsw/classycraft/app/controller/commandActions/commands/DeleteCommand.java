package raf.dsw.classycraft.app.controller.commandActions.commands;

import raf.dsw.classycraft.app.controller.commandActions.AbstractCommand;
import raf.dsw.classycraft.app.gui.swing.painters.ElementPainter;
import raf.dsw.classycraft.app.gui.swing.painters.elementi.InterclassPainter;
import raf.dsw.classycraft.app.gui.swing.painters.veze.ConnectionPainter;
import raf.dsw.classycraft.app.gui.swing.tree.ClassyTreeImplementation;
import raf.dsw.classycraft.app.gui.swing.tree.model.ClassyTreeItem;
import raf.dsw.classycraft.app.gui.swing.view.DiagramView;
import raf.dsw.classycraft.app.gui.swing.view.MainFrame;
import raf.dsw.classycraft.app.model.diagramElements.DiagramElement;
import raf.dsw.classycraft.app.model.diagramElements.connections.Connection;
import raf.dsw.classycraft.app.model.diagramElements.elements.Interclass;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DeleteCommand extends AbstractCommand {
    private DiagramView diagramView;
    private List<ElementPainter> deletedPainters;
    private Point point;
    public DeleteCommand(Point point, DiagramView diagramView){
        deletedPainters = new ArrayList<>();
        this.diagramView = diagramView;
        this.point = point;
    }
    @Override
    public void doCommand() {
        boolean willDelete = false;
        if(deletedPainters.isEmpty()) {
            for (ElementPainter painter : diagramView.getSelectionedRectangles()) {
                if (painter instanceof InterclassPainter) {
                    if (painter.elementAt(painter.getDiagramElement(), point)) {
                        willDelete = true;
                        break;
                    }
                }
            }
            for (ElementPainter painter : diagramView.getSelectionedConnections()) {
                if (painter instanceof ConnectionPainter) {
                    if (painter.elementAt(painter.getDiagramElement(), point)) {
                        willDelete = true;
                        break;
                    }
                }
            }
            // ok, treba da se brise
            if (willDelete) {                                                                                                 // brisem ga U MODELU, saljem notifikaciju u diagramView, i tamo se obrise painter preko iteratora
                for (ElementPainter painter : diagramView.getSelectionedRectangles()) {
                    if (painter instanceof InterclassPainter) {
                        DiagramElement diagramElement = painter.getDiagramElement();
                        if (diagramElement instanceof Interclass) {
                            Interclass interclass = (Interclass) diagramElement;

                            // tmp lista za brisanje
                            List<Connection> connectionsToRemove = new ArrayList<>(interclass.getAllConnectionsOnThisInterclass());
                            // idi kroz listu i brisi
                            for (Connection connection : connectionsToRemove) {
                                interclass.removeAConnectionInThisInterclass(connection);
                                removeElementFromTreeView(connection, diagramView);
                                diagramView.getDiagram().deleteChild(connection);
                            }
                            removeElementFromTreeView(interclass, diagramView);
                            diagramView.getDiagram().deleteChild(interclass);
                        }
                        deletedPainters.add(painter);
                    }
                }
                for (ElementPainter painter : diagramView.getSelectionedConnections()) {
                    if (painter instanceof ConnectionPainter) {
                        DiagramElement interclassOD = ((ConnectionPainter) painter).getInterclassOD();
                        DiagramElement interclassDO = ((ConnectionPainter) painter).getInterclassDO();

                        if (painter.getDiagramElement() instanceof Connection) {
                            Connection connection = (Connection) painter.getDiagramElement();
                            ((Interclass) interclassOD).removeAConnectionInThisInterclass(connection);
                            ((Interclass) interclassDO).removeAConnectionInThisInterclass(connection);
                            removeElementFromTreeView(connection, diagramView);
                            diagramView.getDiagram().deleteChild(connection);
                        }
                        deletedPainters.add(painter);
                    }
                }
            }
            diagramView.getSelectionedRectangles().clear();
            diagramView.getSelectionedConnections().clear();
        }
        else{
            for(ElementPainter painter : deletedPainters){
                diagramView.removePainter(painter);
                diagramView.getDiagram().deleteChild(painter.getDiagramElement());
            }
        }
        ClassyTreeImplementation classyTree = (ClassyTreeImplementation) MainFrame.getInstance().getClassyTree();
        SwingUtilities.updateComponentTreeUI(classyTree.getTreeView());             // refresh celo drvo
        setChangedToTrueInCurrentProject();
    }
    @Override
    public void undoCommand() {
        for(ElementPainter painter : deletedPainters) {
            diagramView.getDiagram().addChild(painter.getDiagramElement());
            diagramView.addPainter(painter);
        }
        diagramView.repaint();
    }

    private void removeElementFromTreeView(DiagramElement diagramElement, DiagramView diagramView) {
        ClassyTreeImplementation classyTree = (ClassyTreeImplementation) MainFrame.getInstance().getClassyTree();
        ClassyTreeItem root = classyTree.getRoot();

        ClassyTreeItem itemToRemove = diagramView.findTheItem(root, diagramElement);
        if (itemToRemove != null) {
            classyTree.getTreeModel().removeNodeFromParent(itemToRemove);  // izbaci item iz parent i updejt
        }
    }
}

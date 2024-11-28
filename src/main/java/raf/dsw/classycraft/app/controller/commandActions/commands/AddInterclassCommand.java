package raf.dsw.classycraft.app.controller.commandActions.commands;

import raf.dsw.classycraft.app.controller.commandActions.AbstractCommand;
import raf.dsw.classycraft.app.core.ApplicationFramework;
import raf.dsw.classycraft.app.gui.swing.messanger.MessageType;
import raf.dsw.classycraft.app.gui.swing.painters.ElementPainter;
import raf.dsw.classycraft.app.gui.swing.painters.element_painters.InterclassPainter;
import raf.dsw.classycraft.app.gui.swing.tree.model.ClassyTreeItem;
import raf.dsw.classycraft.app.gui.swing.view.DiagramView;
import raf.dsw.classycraft.app.gui.swing.view.MainFrame;
import raf.dsw.classycraft.app.model.diagramElements.DiagramElement;
import raf.dsw.classycraft.app.model.diagramElements.elements.Interclass;
import raf.dsw.classycraft.app.model.repositoryModel.ClassyNode;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class AddInterclassCommand extends AbstractCommand {
    private Point location;
    private DiagramElement diagramElement;
    private DiagramView diagramView;
    private ElementPainter painter;
    public AddInterclassCommand(Point location, DiagramElement diagramElement, DiagramView view) {
        this.location = location;
        this.diagramElement = diagramElement;
        this.diagramView = view;
        painter = getInstanceOfPainter(diagramElement);
    }

    @Override
    public void doCommand() {
        Dimension newDimension = null;
        if(diagramElement instanceof Interclass){
            ((Interclass) diagramElement).setConnectionPoints(diagramElement);
            newDimension = ((Interclass) diagramElement).getDimension();
        }

        assert newDimension != null;        // newDimension nikad nece biti null;
        Rectangle elementThatWillBeAdded = new Rectangle(location.x, location.y, newDimension.width, newDimension.height);
        boolean canBeAdded = true;

        for(ElementPainter painter : diagramView.getPainters()) {               // ovde gledam jel se sece sa nekim na ekranu
            if(painter instanceof InterclassPainter){
                Rectangle painterBounds = ((InterclassPainter) painter).getShape().getBounds();
                if(painterBounds.intersects(elementThatWillBeAdded)){
                    canBeAdded = false;
                    break;
                }
            }
        }

        if(canBeAdded){
            ((Interclass) diagramElement).setConnectionPoints(diagramElement);   // ------ ovo sam dodao da bi se konekcione tacke nalazile u modelu umesto u painteru.
            diagramView.dodajMeKaoSubscribera(diagramElement);                  // SVAKI DIJAGRAMELEMENT IMA 1 SUBScriber --- TO JE DIAGRAMVIEW

            diagramView.addPainter(painter);
            if(!diagramView.getDiagram().getChildren().contains(diagramElement))
                diagramView.getDiagram().addChild(diagramElement);
            ClassyTreeItem diagramItem = diagramView.findTheItem(MainFrame.getInstance().getClassyTree().getRoot(), diagramView.getDiagram());
            MainFrame.getInstance().getClassyTree().addChildToDiagram(diagramItem, painter.getDiagramElement());
        }
        else{
            ApplicationFramework.getInstance().getMessageGenerator().generateMessage(
                    "Ne moze ovde da se doda, preklapa se sa postojecim elementom", MessageType.ERROR);
        }
        setChangedToTrueInCurrentProject();
    }
    @Override
    public void undoCommand() {
        diagramView.removePainter(painter);
        if(diagramView.getDiagram().getChildren().contains(diagramElement))
            diagramView.getDiagram().deleteChild(diagramElement);

        ClassyTreeItem parentOfDiagram = findParentOfDiagramInClassyTree(MainFrame.getInstance().getClassyTree().getRoot(), diagramView.getDiagram());
        for(int i = 0; i < Objects.requireNonNull(parentOfDiagram).getChildCount(); i++){
            ClassyTreeItem item = (ClassyTreeItem) parentOfDiagram.getChildAt(i);
            if(item.getClassyNode() == diagramElement){
                parentOfDiagram.remove(i);
                break;
            }
        }
        SwingUtilities.updateComponentTreeUI(MainFrame.getInstance().getClassyTree().getTreeView());
    }
    private ClassyTreeItem findParentOfDiagramInClassyTree(ClassyTreeItem item, ClassyNode diagram) {
        if (item.getClassyNode().equals(diagram.getParent()))
            return item;
        for (int i = 0; i < item.getChildCount(); i++) {
            ClassyTreeItem classyItemNext = diagramView.findTheItem((ClassyTreeItem) item.getChildAt(i), diagram);
            if (classyItemNext != null)
                return classyItemNext;
        }
        return null;
    }
}

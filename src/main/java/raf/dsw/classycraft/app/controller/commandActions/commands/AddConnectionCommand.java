package raf.dsw.classycraft.app.controller.commandActions.commands;

import raf.dsw.classycraft.app.controller.commandActions.AbstractCommand;
import raf.dsw.classycraft.app.gui.swing.painters.ElementPainter;
import raf.dsw.classycraft.app.gui.swing.painters.element_painters.InterclassPainter;
import raf.dsw.classycraft.app.gui.swing.painters.connection_painters.*;
import raf.dsw.classycraft.app.gui.swing.tree.model.ClassyTreeItem;
import raf.dsw.classycraft.app.gui.swing.view.DiagramView;
import raf.dsw.classycraft.app.gui.swing.view.MainFrame;
import raf.dsw.classycraft.app.model.diagramElements.DiagramElement;
import raf.dsw.classycraft.app.model.diagramElements.connections.*;
import raf.dsw.classycraft.app.model.diagramElements.elements.Interclass;
import raf.dsw.classycraft.app.model.repositoryModel.ClassyNode;
import raf.dsw.classycraft.app.model.state.classesAndConnections.Company;

public class AddConnectionCommand extends AbstractCommand {
    private ElementPainter interclassPainterFROM;
    private ElementPainter interclassPainterTO;
    private DiagramView diagramView;
    private DiagramElement diagramElement;
    private ElementPainter elementPainter;
    private DataForConnection dataForConnection;
    private boolean diagramHasThisChild;
    public AddConnectionCommand(ElementPainter interclassPainterFROM, ElementPainter interclassPainterTO,
                                DiagramView diagramView, DataForConnection dataForConnection) {
        this.interclassPainterFROM = interclassPainterFROM;
        this.interclassPainterTO = interclassPainterTO;
        this.diagramView = diagramView;
        this.dataForConnection = dataForConnection;
        this.diagramHasThisChild = false;
    }
    public AddConnectionCommand(Interclass interclassPainterFROM, Interclass interclassPainterTO,
                                DiagramView diagramView, DataForConnection dataForConnection, boolean diagramHasThisChild) {
        this.interclassPainterFROM = new InterclassPainter(interclassPainterFROM);
        this.interclassPainterTO = new InterclassPainter(interclassPainterTO);
        this.diagramView = diagramView;
        this.dataForConnection = dataForConnection;
        this.diagramHasThisChild = diagramHasThisChild;
    }

    @Override
    public void doCommand() {
        if(interclassPainterFROM != null && interclassPainterTO != null){
            // Abstraktna fabrika za veze za bonus
            Company company = new Company();
            DiagramElement diagramElement = company.createConnection(dataForConnection);
            diagramView.dodajMeKaoSubscribera(diagramElement);
            this.diagramElement = diagramElement;
            this.elementPainter = instantiatePainter(diagramElement);

            // ovo moramo da uradimo zbog brisanja kasnije !!! ako se obrise klasa koja ima veze, moraju i njene veze da se obrisu
            if(interclassPainterFROM instanceof InterclassPainter && interclassPainterTO instanceof InterclassPainter){
                DiagramElement tmp = interclassPainterFROM.getDiagramElement();
                if(tmp instanceof Interclass){
                    ((Interclass) tmp).addAConnectionInThisInterclass(((Connection) diagramElement));
                }
                DiagramElement tmp2 = interclassPainterTO.getDiagramElement();
                if(tmp2 instanceof Interclass){
                    ((Interclass) tmp2).addAConnectionInThisInterclass(((Connection) diagramElement));
                }
            }
            diagramView.dodajMeKaoSubscribera(diagramElement);
            diagramView.addPainter(elementPainter);
            if(!diagramHasThisChild) {
                diagramView.getDiagram().addChild(diagramElement);          // tehnicki tacno ali ne zelim je u stablu
                diagramHasThisChild = true;
            }
            ClassyTreeItem root = MainFrame.getInstance().getClassyTree().getRoot();
            ClassyNode diagram = diagramView.getDiagram();
            ClassyTreeItem classyTreeItem = diagramView.findTheItem(root, diagram);
            MainFrame.getInstance().getClassyTree().addChildToDiagram(classyTreeItem, elementPainter.getDiagramElement());   // vec objasnjeno u addInterclassState
        }
        diagramView.repaint();
        setChangedToTrueInCurrentProject();
    }

    private ElementPainter instantiatePainter(DiagramElement diagramElement) {
        Interclass interclassOD = (Interclass) interclassPainterFROM.getDiagramElement();
        Interclass interclassDO = (Interclass) interclassPainterTO.getDiagramElement();


        ElementPainter elementPainter = null;
        if(diagramElement instanceof AssociationConnection){
            elementPainter = new AssociationPainter(interclassOD, interclassDO, diagramElement);            /// PROMENJENO JE sa interclassPainter na interclassu ZBOG KONEKCIONIH TACAKA
        }
        else if(diagramElement instanceof InheritanceConnection){
            elementPainter = new InheritancePainter(interclassOD, interclassDO, diagramElement);
        }
        else if(diagramElement instanceof RealisationConnection){
            elementPainter = new RealisationPainter(interclassOD, interclassDO, diagramElement);
        }
        else if(diagramElement instanceof DependencyConnection){
            elementPainter = new DependencyPainter(interclassOD, interclassDO, diagramElement);
        }
        else if(diagramElement instanceof AggregationConnection){
            elementPainter = new AggregationPainter(interclassOD, interclassDO, diagramElement);
        }
        else if(diagramElement instanceof CompositionConnection){
            elementPainter = new CompositionPainter(interclassOD, interclassDO, diagramElement);
        }
        return elementPainter;
    }

    @Override
    public void undoCommand() {
        diagramView.removePainter(elementPainter);
        diagramView.getDiagram().deleteChild(diagramElement);
    }
}

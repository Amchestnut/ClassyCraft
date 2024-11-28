package raf.dsw.classycraft.app.controller.commandActions.commands;

import raf.dsw.classycraft.app.controller.commandActions.AbstractCommand;
import raf.dsw.classycraft.app.core.ApplicationFramework;
import raf.dsw.classycraft.app.gui.swing.messanger.MessageType;
import raf.dsw.classycraft.app.gui.swing.painters.ElementPainter;
import raf.dsw.classycraft.app.gui.swing.painters.element_painters.InterclassPainter;
import raf.dsw.classycraft.app.gui.swing.view.DiagramView;
import raf.dsw.classycraft.app.model.diagramElements.DiagramElement;
import raf.dsw.classycraft.app.model.diagramElements.elements.Interclass;

import java.awt.*;

public class AddDuplicateCommand extends AbstractCommand {
    private DiagramView diagramView;
    private ElementPainter painter;
    private DiagramElement diagramElement;
    private Point currentPoint;
    public AddDuplicateCommand(DiagramView diagramView, DiagramElement diagramElement, Point currentPoint) {
        this.diagramView = diagramView;
        this.diagramElement = diagramElement;
        this.currentPoint = currentPoint;
    }

    @Override
    public void doCommand() {
        // First a check to see if on the current point, our new rectangle, intersects with any existing (so we take the point and the dimension of the new rectangle)
        Dimension dimension = null;
        if(diagramElement instanceof Interclass){
            dimension = ((Interclass) diagramElement).getDimension();
        }

        Rectangle rectangleOfTheNewElement = null;
        if(dimension != null){
            rectangleOfTheNewElement = new Rectangle(currentPoint.x, currentPoint.y, dimension.width, dimension.height);
        }

        // Then we check if he suddently intersects with any existing
        boolean canBeAdded = true;

        for(ElementPainter painter : diagramView.getPainters()) {
            if(painter instanceof InterclassPainter){
                Rectangle painterBounds = ((InterclassPainter) painter).getShape().getBounds();
                if(rectangleOfTheNewElement != null){
                    if(painterBounds.intersects(rectangleOfTheNewElement)){
                        canBeAdded = false;
                        break;
                    }
                }
            }
        }

        if(canBeAdded){
            if(diagramElement instanceof Interclass){
                ((Interclass) diagramElement).setLocation(currentPoint);

                this.painter = getInstanceOfPainter(diagramElement);

                diagramView.dodajMeKaoSubscribera(diagramElement);
                diagramView.addPainter(painter);
                diagramView.getDiagram().addChild(diagramElement);
            }
        }
        else{
            ApplicationFramework.getInstance().getMessageGenerator().generateMessage(
                    "This duplicate you want to add is intersecting with someone on the workscreen", MessageType.ERROR);
        }
        diagramView.getSelectionedRectangles().clear();
        diagramView.backToOriginalColor();
        setChangedToTrueInCurrentProject();
    }

    @Override
    public void undoCommand() {
        diagramView.removePainter(painter);
        diagramView.getDiagram().deleteChild(diagramElement);
    }
}

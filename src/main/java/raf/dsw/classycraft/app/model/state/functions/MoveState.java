package raf.dsw.classycraft.app.model.state.functions;

import raf.dsw.classycraft.app.controller.commandActions.AbstractCommand;
import raf.dsw.classycraft.app.controller.commandActions.commands.MoveCommand;
import raf.dsw.classycraft.app.core.ApplicationFramework;
import raf.dsw.classycraft.app.gui.swing.messanger.MessageType;
import raf.dsw.classycraft.app.gui.swing.painters.ElementPainter;
import raf.dsw.classycraft.app.gui.swing.painters.element_painters.InterclassPainter;
import raf.dsw.classycraft.app.gui.swing.view.DiagramView;
import raf.dsw.classycraft.app.model.diagramElements.DiagramElement;
import raf.dsw.classycraft.app.model.diagramElements.elements.Interclass;
import raf.dsw.classycraft.app.model.state.State;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    I want to move a class on the screen only if it is selected.
    - 1) I will check if the click is located on ANY SELECTED PAINTER.
        --- This means I will iterate through the list of selected elements and see if any selected painter is at the point where I clicked.
    - 2) If it is, I will set "isDragging" to true.
    - 3) For the "mouseDragged" method to even be triggered, "isDragging" must be set to true.
    - 4) If "isDragging" is true and the user starts dragging, ALL SELECTED RECTANGLES will be dragged!!!
    - 5) When the mouse is released, the dragging operation is complete, and the point resets to null.
 */
public class MoveState implements State {

    private Point initialClickPoint = null;
    private boolean isDragging = false;
    private Map<InterclassPainter, Point> initialLocations = new HashMap<>();
    private Map<InterclassPainter, Point> newLocations = new HashMap<>();


    @Override
    public void misKliknut(int x, int y, DiagramView diagramView) {
        initialClickPoint = new Point(x,y); // The original click is saved here, and he is not changed during drag (until we call everything again)
        isDragging = false;

        for (ElementPainter painter : diagramView.getSelectionedRectangles()) {
            if (painter instanceof InterclassPainter) {
                DiagramElement diagramElement = painter.getDiagramElement();

                if (diagramElement instanceof Interclass) {
                    initialLocations.put((InterclassPainter) painter, ((Interclass) diagramElement).getLocation());
                    newLocations.put((InterclassPainter) painter, ((Interclass) diagramElement).getLocation());
                }
                if (painter.elementAt(painter.getDiagramElement(), initialClickPoint)) {
                    isDragging = true;
                }
            }
        }
    }

    @Override
    public void misOtpusten(int x, int y, DiagramView diagramView) {
        boolean itIntersectsWithSomeone = false;

        List<ElementPainter> unselectedPainters = new ArrayList<>();

        for (ElementPainter painter : diagramView.getPainters()) {     // Fill up the list with unselected painters
            if (!diagramView.getSelectionedRectangles().contains(painter)) {
                unselectedPainters.add(painter);
            }
        }

        for (ElementPainter painter : diagramView.getSelectionedRectangles()) { // if intersects
            if(painter instanceof InterclassPainter){
                Rectangle painterBounds = ((InterclassPainter) painter).getShape().getBounds();

                for(ElementPainter unselected : unselectedPainters){
                    if(unselected instanceof InterclassPainter){
                        Rectangle boundsOfUnselectedPainter = ((InterclassPainter) unselected).getShape().getBounds();
                        if(painterBounds.intersects(boundsOfUnselectedPainter)){
                            itIntersectsWithSomeone = true;
                        }
                    }
                }
            }
        }

        if(itIntersectsWithSomeone){    // If intersects with anyone, return them back to original location
            for (ElementPainter selectedPainter : diagramView.getSelectionedRectangles()) {
                if(selectedPainter instanceof InterclassPainter){
                    DiagramElement diagramElement = selectedPainter.getDiagramElement();
                    Point lastLocation = null;

                    if(initialLocations.containsKey(selectedPainter)){
                        lastLocation = initialLocations.get(selectedPainter);
                    }
                    if(lastLocation != null){
                        ((Interclass) diagramElement).setLocation(lastLocation);
                        ((Interclass) diagramElement).setConnectionPoints(diagramElement);
                    }
                }
            }
            ApplicationFramework.getInstance().getMessageGenerator().generateMessage(
                    "You can't move the element here, because he intersects with someone!", MessageType.ERROR);
        }
        else {
            AbstractCommand command = new MoveCommand(initialLocations, newLocations);
            diagramView.getCommandManager().addCommand(command);
        }
    }
    @Override
    public void misPrevucen(int x, int y, DiagramView diagramView) {
        if (isDragging) {
            int dx = x - initialClickPoint.x;    // If i moved the mouse to the right, dx = current DRAG point  -   original DRAG point
            int dy = y - initialClickPoint.y;

            for (ElementPainter selectedPainter : diagramView.getSelectionedRectangles()) {
                if(selectedPainter instanceof InterclassPainter){
                    DiagramElement diagramElement = selectedPainter.getDiagramElement();
                    Point lastLocation = initialLocations.get(selectedPainter);

                    if(lastLocation != null){
                        Point newLocation = new Point(lastLocation.x + dx, lastLocation.y + dy);
                        ((Interclass) diagramElement).setLocation(newLocation);
                        ((Interclass) diagramElement).setConnectionPoints(diagramElement);

                        // Update the new location in the newLocations map - for undo-redo
                        newLocations.put((InterclassPainter) selectedPainter, ((Interclass)diagramElement).getLocation());
                    }
                }
            }
        }
    }
}


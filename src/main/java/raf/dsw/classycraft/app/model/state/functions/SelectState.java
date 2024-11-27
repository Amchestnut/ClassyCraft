package raf.dsw.classycraft.app.model.state.functions;

import raf.dsw.classycraft.app.gui.swing.painters.ElementPainter;
import raf.dsw.classycraft.app.gui.swing.painters.element_painters.InterclassPainter;
import raf.dsw.classycraft.app.gui.swing.painters.connection_painters.ConnectionPainter;
import raf.dsw.classycraft.app.gui.swing.view.DiagramView;
import raf.dsw.classycraft.app.model.diagramElements.DiagramElement;
import raf.dsw.classycraft.app.model.diagramElements.connections.Connection;
import raf.dsw.classycraft.app.model.state.State;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/*
      - Iterate through all painters.
      - If the instance is an interclass (this is necessary for casting, and it's a cool feature because if it's a connection, we won't "change" it, only select it).
      - If a painter is located at the point currently CLICKED,
      - Change its color to black.

      - This means I need to change the color to black in the MODEL.
      - When the color is changed in the MODEL, it triggers a notify, which immediately causes a repaint (technically, repaint happens as soon as any change occurs).
 */

public class SelectState implements State {

    private Point pocetnaTackaZaPravougaonik;

    @Override
    public void misKliknut(int x, int y, DiagramView diagramView) {

        pocetnaTackaZaPravougaonik = new Point(x, y);

        boolean somethingWasSelectedBefore = false;
        if(!diagramView.getSelectionedRectangles().isEmpty()){       // If any was clicked before, kick him off to create a new one
            diagramView.getSelectionedRectangles().clear();
            somethingWasSelectedBefore = true;
        }
        if(!diagramView.getSelectionedConnections().isEmpty()){
            diagramView.getSelectionedConnections().clear();
            somethingWasSelectedBefore = true;
        }

        if(somethingWasSelectedBefore){
            diagramView.backToOriginalColor();
        }

        for(ElementPainter painter : diagramView.getPainters()){
            if(painter instanceof InterclassPainter){   // If yes, I want to change his color
                if(painter.elementAt(painter.getDiagramElement(), new Point(x, y))){   // If he is there, TRUE
                    DiagramElement diagramElement = painter.getDiagramElement();
                    diagramElement.setColor(0xFFFFFFFF);
                    diagramView.addRectangleToSelected(painter);
                }
            }

            else if(painter instanceof ConnectionPainter){
                if(painter.elementAt(painter.getDiagramElement(), new Point(x, y))){
                    DiagramElement diagramElement = painter.getDiagramElement();
                    diagramElement.setColor(0xFFFFA500);
                    diagramView.addConnectionsToSelected(painter);
                }
            }
        }
    }

    @Override
    public void misOtpusten(int x, int y, DiagramView diagramView) {
        diagramView.setSelectionRectangle(null);
        diagramView.repaint();
    }

    @Override
    public void misPrevucen(int x, int y, DiagramView diagramView) {
        diagramView.backToOriginalColor();
        diagramView.getSelectionedRectangles().clear();
        diagramView.getSelectionedConnections().clear();

        int rectX = Math.min((int)pocetnaTackaZaPravougaonik.getX(), x);
        int rectY = Math.min((int)pocetnaTackaZaPravougaonik.getY(), y);
        int rectWidth = Math.abs(x - (int)pocetnaTackaZaPravougaonik.getX());
        int rectHeight = Math.abs(y - (int)pocetnaTackaZaPravougaonik.getY());

        Rectangle selectionRect = new Rectangle(rectX, rectY, rectWidth, rectHeight);
        diagramView.setSelectionRectangle(selectionRect);

        for(ElementPainter painter : diagramView.getPainters()) {
            // Does the rectangle intersects with classes?
            if(painter instanceof InterclassPainter){
                Rectangle painterBounds = ((InterclassPainter) painter).getShape().getBounds();
                if(painterBounds.intersects(selectionRect)){
                    DiagramElement diagramElement = painter.getDiagramElement();
                    diagramElement.setColor(0xFFFFFFFF);                      // This is handled by observer
                    diagramView.addRectangleToSelected(painter);
                }
            }
            // Does the rectangle intersects with connections?
            else if (painter instanceof ConnectionPainter) {
                Rectangle2D rectangleAroundConnection = ((ConnectionPainter) painter).getRectangleAroundConnection();
                if (rectangleAroundConnection != null && rectangleAroundConnection.intersects(selectionRect)) {
                    DiagramElement diagramElement = painter.getDiagramElement();
                    if (diagramElement instanceof Connection) {
                        diagramElement.setColor(0xFFFFA500);
                        diagramView.addConnectionsToSelected(painter);
                    }
                }
            }
        }
        diagramView.repaint();
    }
}
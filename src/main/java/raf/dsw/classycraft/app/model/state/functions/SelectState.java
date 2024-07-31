package raf.dsw.classycraft.app.model.state.functions;

import raf.dsw.classycraft.app.gui.swing.painters.ElementPainter;
import raf.dsw.classycraft.app.gui.swing.painters.elementi.InterclassPainter;
import raf.dsw.classycraft.app.gui.swing.painters.veze.ConnectionPainter;
import raf.dsw.classycraft.app.gui.swing.view.DiagramView;
import raf.dsw.classycraft.app.model.diagramElements.DiagramElement;
import raf.dsw.classycraft.app.model.diagramElements.connections.Connection;
import raf.dsw.classycraft.app.model.state.State;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/*
      -Prolazimo kroz sve paintere
      - ako je instanca interclasa (moramo zbog castovanja, a i super je fora, jer ako je veza, necemo je uopste "menjati" nego samo selektovati)
      - ako se neki painter nalazi na tacki koja je trenutno KLIKNUTA
      - promeni mu boju u crnu

      - znaci moram u MODELU da promenim boju na crnu
      - kad promenim boju u MODELU, trigeruje se notify, i ide odma repaint (tehnicki cim se bilo sta promeni)
 */
public class SelectState implements State {

    private Point pocetnaTackaZaPravougaonik;

    @Override
    public void misKliknut(int x, int y, DiagramView diagramView) {

        pocetnaTackaZaPravougaonik = new Point(x, y);

        boolean somethingWasSelectedBefore = false;
        if(!diagramView.getSelectionedRectangles().isEmpty()){       // ako je neki vec bio kliknut, izbaci ga iz liste da bi kliknuo novi
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
            if(painter instanceof InterclassPainter){               // ako jeste, onda zelim da mu promenim boju
                if(painter.elementAt(painter.getDiagramElement(), new Point(x, y))){    // ako se nalazi tu, TRUE
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
            // Da li pravougaonik sece klase?
            if(painter instanceof InterclassPainter){
                Rectangle painterBounds = ((InterclassPainter) painter).getShape().getBounds();
                if(painterBounds.intersects(selectionRect)){
                    DiagramElement diagramElement = painter.getDiagramElement();
                    diagramElement.setColor(0xFFFFFFFF);                      // ovo sredjuje observer
                    diagramView.addRectangleToSelected(painter);
                }
            }
            // da li pravougaonik sece linije?
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
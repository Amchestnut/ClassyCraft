package raf.dsw.classycraft.app.model.state.connections;

import raf.dsw.classycraft.app.controller.commandActions.AbstractCommand;
import raf.dsw.classycraft.app.controller.commandActions.commands.AddConnectionCommand;
import raf.dsw.classycraft.app.gui.swing.painters.ElementPainter;
import raf.dsw.classycraft.app.gui.swing.painters.element_painters.InterclassPainter;
import raf.dsw.classycraft.app.gui.swing.view.DiagramView;
import raf.dsw.classycraft.app.model.diagramElements.DiagramElement;
import raf.dsw.classycraft.app.model.diagramElements.connections.DataForConnection;
import raf.dsw.classycraft.app.model.diagramElements.elements.Interclass;
import raf.dsw.classycraft.app.model.state.State;

import java.awt.*;
import java.awt.geom.Line2D;

public class AddConnectionState implements State {

    private ElementPainter interclassPainterFROM = null;
    private ElementPainter interclassPainterTO = null;
    private Point initialPointClicked = null;
    @Override
    public void misKliknut(int x, int y, DiagramView diagramView) {
        initialPointClicked = new Point(x, y);

        for(ElementPainter painter : diagramView.getPainters()){
            if(painter instanceof InterclassPainter){
                if (painter.elementAt(painter.getDiagramElement(), initialPointClicked)) {
                    interclassPainterFROM = painter;

                    break;
                }
            }
        }
    }

    @Override
    public void misOtpusten(int x, int y, DiagramView diagramView) {
        diagramView.setTemporaryLine(null);
        diagramView.repaint();   // BUGFIX, repaint in order to immediately delete this temporary line when we release the mouse!

        if(interclassPainterFROM instanceof InterclassPainter && interclassPainterTO instanceof InterclassPainter){
            DiagramElement diagramElement1 = interclassPainterFROM.getDiagramElement();
            DiagramElement diagramElement2 = interclassPainterTO.getDiagramElement();
            if(diagramElement1.equals(diagramElement2)){
                interclassPainterFROM = null;
                interclassPainterTO = null;
                return;
            }

            // Here we get the data for connections. Here we check if its NULL, and never later
            DataForConnection dataForConnection = diagramView.kojuVezuHoceUser();
            if(dataForConnection == null)
                return;
            dataForConnection.setInterclassOD((Interclass) interclassPainterFROM.getDiagramElement());
            dataForConnection.setInterclassDO((Interclass) interclassPainterTO.getDiagramElement());

            AbstractCommand command = new AddConnectionCommand(interclassPainterFROM, interclassPainterTO, diagramView, dataForConnection);
            diagramView.getCommandManager().addCommand(command);
        }
        interclassPainterFROM = null;
        interclassPainterTO = null;
    }

    @Override
    public void misPrevucen(int x, int y, DiagramView diagramView) {
        if(interclassPainterFROM != null){
            Line2D linija = new Line2D.Double(new Point(initialPointClicked.x, initialPointClicked.y), new Point(x, y));
            diagramView.setTemporaryLine(linija);

            interclassPainterTO = null;  // BIG BRAIN BUGFIX, always set to null this "TO", if not, during mouse drag we would always get the class from where it starts, so it draw the connection on himself!!!

            for(ElementPainter painter : diagramView.getPainters()){
                if(painter instanceof InterclassPainter){
                    if (painter.elementAt(painter.getDiagramElement(), new Point(x,y))) {
                        interclassPainterTO = painter;
                        break;
                    }
                }
            }
            diagramView.repaint();
        }
    }
}

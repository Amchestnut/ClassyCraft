package raf.dsw.classycraft.app.model.state.functions;

import raf.dsw.classycraft.app.controller.commandActions.commands.EditCommand;
import raf.dsw.classycraft.app.gui.swing.painters.ElementPainter;
import raf.dsw.classycraft.app.gui.swing.painters.element_painters.InterclassPainter;
import raf.dsw.classycraft.app.gui.swing.painters.connection_painters.ConnectionPainter;
import raf.dsw.classycraft.app.gui.swing.view.DiagramView;
import raf.dsw.classycraft.app.model.diagramElements.DiagramElement;
import raf.dsw.classycraft.app.model.state.State;

import java.awt.*;

public class EditState implements State {
    @Override
    public void misKliknut(int x, int y, DiagramView diagramView) {
        DiagramElement diagramElement = null;
        // We want to check if the current mouse click intersects with any existing painter
        for (ElementPainter painter : diagramView.getPainters()) {
            if (painter instanceof InterclassPainter) {
                if (painter.elementAt(painter.getDiagramElement(), new Point(x, y))) {
                    diagramElement = painter.getDiagramElement();
                }
            } else if (painter instanceof ConnectionPainter) {
                if (painter.elementAt(painter.getDiagramElement(), new Point(x, y))) {
                    diagramElement = painter.getDiagramElement();
                }
            }
        }
        EditCommand command = new EditCommand(diagramView, diagramElement);
        if(!command.isNothingHasBeenDone()){
            return;
        }
        diagramView.getCommandManager().addCommand(command);
    }

    @Override
    public void misOtpusten ( int x, int y, DiagramView diagramView){
    }

    @Override
    public void misPrevucen ( int x, int y, DiagramView diagramView){
    }
}
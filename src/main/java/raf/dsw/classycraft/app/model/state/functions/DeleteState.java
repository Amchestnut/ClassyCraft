package raf.dsw.classycraft.app.model.state.functions;

import raf.dsw.classycraft.app.controller.commandActions.AbstractCommand;
import raf.dsw.classycraft.app.controller.commandActions.commands.DeleteCommand;
import raf.dsw.classycraft.app.gui.swing.view.DiagramView;
import raf.dsw.classycraft.app.model.state.State;

import java.awt.*;

public class DeleteState implements State {

    @Override
    public void misKliknut(int x, int y, DiagramView diagramView) {
        AbstractCommand command = new DeleteCommand(new Point(x, y), diagramView);
        diagramView.getCommandManager().addCommand(command);
    }
    @Override
    public void misOtpusten(int x, int y, DiagramView diagramView) {}

    @Override
    public void misPrevucen(int x, int y, DiagramView diagramView) {}
}

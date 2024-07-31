package raf.dsw.classycraft.app.model.state.classesAndConnections;

import raf.dsw.classycraft.app.controller.commandActions.AbstractCommand;
import raf.dsw.classycraft.app.controller.commandActions.commands.AddInterclassCommand;
import raf.dsw.classycraft.app.gui.swing.view.DiagramView;
import raf.dsw.classycraft.app.model.diagramElements.DiagramElement;
import raf.dsw.classycraft.app.model.diagramElements.elements.DataForElementFromDialog;
import raf.dsw.classycraft.app.model.state.State;

import java.awt.*;

public class AddInterclassState implements State {

    @Override
    public void misKliknut(int x, int y, DiagramView diagramView) {
        Point point = new Point(x, y);

        DataForElementFromDialog data = diagramView.kojuInterklasuHoceUser();
        if(data == null)
            return;

        Company company = new Company();
        DiagramElement diagramElement = company.createInterclass(point, data);

        AbstractCommand command = new AddInterclassCommand(point, diagramElement, diagramView);
        diagramView.getCommandManager().addCommand(command);
    }

    @Override
    public void misOtpusten(int x, int y, DiagramView diagramView) {

    }
    @Override
    public void misPrevucen(int x, int y, DiagramView diagramView) {

    }
}
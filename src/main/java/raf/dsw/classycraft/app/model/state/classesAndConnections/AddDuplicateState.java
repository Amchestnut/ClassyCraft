package raf.dsw.classycraft.app.model.state.classesAndConnections;

import raf.dsw.classycraft.app.controller.commandActions.AbstractCommand;
import raf.dsw.classycraft.app.controller.commandActions.commands.AddDuplicateCommand;
import raf.dsw.classycraft.app.core.ApplicationFramework;
import raf.dsw.classycraft.app.gui.swing.messanger.MessageType;
import raf.dsw.classycraft.app.gui.swing.painters.ElementPainter;
import raf.dsw.classycraft.app.gui.swing.view.DiagramView;
import raf.dsw.classycraft.app.model.diagramElements.DiagramElement;
import raf.dsw.classycraft.app.model.state.State;

import java.awt.*;

public class AddDuplicateState implements State {
    @Override
    public void misKliknut(int x, int y, DiagramView diagramView) {
        ElementPainter theOneThatWillBeAdded = null;
        DiagramElement diagramElement = null;

        // First goes the check to see if only 1 is selected!
        if(diagramView.getSelectionedRectangles().isEmpty()){
            ApplicationFramework.getInstance().getMessageGenerator().generateMessage(
                    "You dont have any selected element to duplicate!", MessageType.ERROR);
        }
        else if(diagramView.getSelectionedRectangles().size() > 1){
            ApplicationFramework.getInstance().getMessageGenerator().generateMessage(
                    "You have too much elements selected, pick only 1!", MessageType.ERROR);
        }
        else if(diagramView.getSelectionedRectangles().size() == 1){
            theOneThatWillBeAdded = diagramView.getSelectionedRectangles().get(0);
        }


        if(theOneThatWillBeAdded != null){
            diagramElement = theOneThatWillBeAdded.getDiagramElement().clone(); // clone !
        }

        AbstractCommand command = new AddDuplicateCommand(diagramView, diagramElement, new Point(x, y));
        diagramView.getCommandManager().addCommand(command);
    }

    @Override
    public void misOtpusten(int x, int y, DiagramView diagramView) {}
    @Override
    public void misPrevucen(int x, int y, DiagramView diagramView) {}
}

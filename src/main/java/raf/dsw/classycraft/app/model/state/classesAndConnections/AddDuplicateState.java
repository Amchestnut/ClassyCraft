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

        // prvo ide provera da vidimo da li je samo jedan jedini selektovan.
        if(diagramView.getSelectionedRectangles().isEmpty()){
            ApplicationFramework.getInstance().getMessageGenerator().generateMessage(
                    "Nemate nijedan selektovani Element da bi ste ga duplirali", MessageType.ERROR);
        }
        else if(diagramView.getSelectionedRectangles().size() > 1){
            ApplicationFramework.getInstance().getMessageGenerator().generateMessage(
                    "Previse selektovanih Elemenata, izaberite samo jedan", MessageType.ERROR);
        }
        else if(diagramView.getSelectionedRectangles().size() == 1){
            theOneThatWillBeAdded = diagramView.getSelectionedRectangles().get(0);
        }


        if(theOneThatWillBeAdded != null){
            diagramElement = theOneThatWillBeAdded.getDiagramElement().clone();                    // clone !
        }

        AbstractCommand command = new AddDuplicateCommand(diagramView, diagramElement, new Point(x, y));
        diagramView.getCommandManager().addCommand(command);
    }

    @Override
    public void misOtpusten(int x, int y, DiagramView diagramView) {}
    @Override
    public void misPrevucen(int x, int y, DiagramView diagramView) {}
}

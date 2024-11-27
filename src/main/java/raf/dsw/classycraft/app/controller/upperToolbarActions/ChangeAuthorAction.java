package raf.dsw.classycraft.app.controller.upperToolbarActions;

import raf.dsw.classycraft.app.controller.AbstractClassyAction;
import raf.dsw.classycraft.app.core.ApplicationFramework;
import raf.dsw.classycraft.app.gui.swing.messanger.MessageType;
import raf.dsw.classycraft.app.gui.swing.view.MainFrame;
import raf.dsw.classycraft.app.model.modelImplementation.Diagram;
import raf.dsw.classycraft.app.model.modelImplementation.Project;
import raf.dsw.classycraft.app.model.repositoryModel.ClassyNode;
import raf.dsw.classycraft.app.model.repositoryModel.ClassyNodeComposite;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class ChangeAuthorAction extends AbstractClassyAction {
    public ChangeAuthorAction() {
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_MASK));
        putValue(SMALL_ICON, scaleIcon(loadIcon("/images/icon_changeauthor.png"), 32, 32));
        putValue(NAME, "Change Author");
        putValue(SHORT_DESCRIPTION, "Change author");
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        if(MainFrame.getInstance().getClassyTree().getSelectedNode() != null) {
            ClassyNode selected = MainFrame.getInstance().getClassyTree().getSelectedNode().getClassyNode();
            if (selected instanceof Diagram) {
                ApplicationFramework.getInstance().getMessageGenerator().generateMessage("You need to select a Project!", MessageType.ERROR);
            } else if (selected instanceof ClassyNodeComposite) {
                if (!(selected instanceof Project)) {
                    ApplicationFramework.getInstance().getMessageGenerator().generateMessage("You need to select a Project!", MessageType.ERROR);
                    return;
                }
                String entered = MainFrame.getInstance().enterAuthorNewName();
                if (entered != null) {
                    ((Project) selected).setAuthor(entered);
                }
            }
        }
        else{
            ApplicationFramework.getInstance().getMessageGenerator().generateMessage("You need to select a Project!", MessageType.ERROR);
        }
    }
}

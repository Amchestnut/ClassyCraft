package raf.dsw.classycraft.app.controller.upperToolbarActions;

import raf.dsw.classycraft.app.controller.AbstractClassyAction;
import raf.dsw.classycraft.app.core.ApplicationFramework;
import raf.dsw.classycraft.app.gui.swing.messanger.MessageType;
import raf.dsw.classycraft.app.gui.swing.view.MainFrame;
import raf.dsw.classycraft.app.model.modelImplementation.Project;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;

public class SaveProjectAction extends AbstractClassyAction {
    public SaveProjectAction(){
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
        putValue(SMALL_ICON, scaleIcon(loadIcon("/images/save.png"), 32, 32));
        putValue(NAME, "Save project");
        putValue(SHORT_DESCRIPTION, "Save project");
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser jFileChooser = new JFileChooser();

        if(MainFrame.getInstance().getClassyTree().getSelectedNode() == null ||
                !(MainFrame.getInstance().getClassyTree().getSelectedNode().getClassyNode() instanceof Project)){
            ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Morate selektovati neki Project!", MessageType.ERROR);
            return;
        }

        Project project = (Project) MainFrame.getInstance().getClassyTree().getSelectedNode().getClassyNode();
        if(project.isChanged()) {
            File projectFile = null;
            if (project.getPath() == null || project.getPath().isEmpty()) {
                if (jFileChooser.showSaveDialog(MainFrame.getInstance()) == JFileChooser.APPROVE_OPTION) {
                    projectFile = jFileChooser.getSelectedFile();
                    project.setPath(projectFile.getPath());
                } else {
                    return;
                }
            }
        }
        project.setChanged(false);
        MainFrame.getInstance().getActionManager().getSaveProjectAction().setEnabled(false);
        ApplicationFramework.getInstance().getSerializer().saveProject(project);
        project.setPath("");
    }
}

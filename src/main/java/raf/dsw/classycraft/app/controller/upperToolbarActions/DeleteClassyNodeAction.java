package raf.dsw.classycraft.app.controller.upperToolbarActions;

import raf.dsw.classycraft.app.controller.AbstractClassyAction;
import raf.dsw.classycraft.app.core.ApplicationFramework;
import raf.dsw.classycraft.app.gui.swing.messanger.MessageType;
import raf.dsw.classycraft.app.gui.swing.tree.model.ClassyTreeItem;
import raf.dsw.classycraft.app.gui.swing.view.MainFrame;
import raf.dsw.classycraft.app.model.modelImplementation.Project;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class DeleteClassyNodeAction extends AbstractClassyAction {

    public DeleteClassyNodeAction() {
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
        putValue(SMALL_ICON, scaleIcon(loadIcon("/images/icon_deleteproject.png"), 32, 32));
        putValue(NAME, "Delete node");
        putValue(SHORT_DESCRIPTION, "Delete node");
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        ClassyTreeItem selected = MainFrame.getInstance().getClassyTree().getSelectedNode();
        if(selected == null){
            ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Morate selektovati node da biste ga izbrisali!", MessageType.ERROR);
            return;
        }
        if(selected.getClassyNode() instanceof Project){
            ((Project) selected.getClassyNode()).removeFromParent();
        }
        MainFrame.getInstance().getClassyTree().deleteNode(selected);
    }
}

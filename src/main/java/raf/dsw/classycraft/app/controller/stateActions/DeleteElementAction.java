package raf.dsw.classycraft.app.controller.stateActions;

import raf.dsw.classycraft.app.controller.AbstractClassyAction;
import raf.dsw.classycraft.app.gui.swing.view.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class DeleteElementAction extends AbstractClassyAction {
    public DeleteElementAction() {
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F5, ActionEvent.ALT_MASK));
        putValue(SMALL_ICON, scaleIcon(loadIcon("/images/icon_delete_element.png"), 32, 32));
        putValue(NAME, "Delete element");
        putValue(SHORT_DESCRIPTION, "Delete element");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        MainFrame.getInstance().getPackageView().startDeleteState();
    }
}

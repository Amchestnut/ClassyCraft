package raf.dsw.classycraft.app.controller.upperToolbarActions;

import raf.dsw.classycraft.app.controller.AbstractClassyAction;
import raf.dsw.classycraft.app.gui.swing.view.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class UndoAction extends AbstractClassyAction {
    public UndoAction() {
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
        putValue(SMALL_ICON, scaleIcon(loadIcon("/images/undo.png"), 32, 32));
        putValue(NAME, "Undo");
        putValue(SHORT_DESCRIPTION, "Undo");
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        MainFrame.getInstance().getPackageView().getCurrentCommandManager().undoCommand();
    }
}

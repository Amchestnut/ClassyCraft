package raf.dsw.classycraft.app.controller.upperToolbarActions;

import raf.dsw.classycraft.app.controller.AbstractClassyAction;
import raf.dsw.classycraft.app.gui.swing.view.UserGuideFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class GuideAction extends AbstractClassyAction {
    public GuideAction(){
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
        putValue(SMALL_ICON, scaleIcon(loadIcon("/images/icon_userguide.png"), 32, 32));
        putValue(NAME, "User guide");
        putValue(SHORT_DESCRIPTION, "User guide");
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        new UserGuideFrame();
    }

}
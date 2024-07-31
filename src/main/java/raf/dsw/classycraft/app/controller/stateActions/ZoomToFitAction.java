package raf.dsw.classycraft.app.controller.stateActions;

import raf.dsw.classycraft.app.controller.AbstractClassyAction;
import raf.dsw.classycraft.app.gui.swing.view.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class ZoomToFitAction extends AbstractClassyAction {
    public ZoomToFitAction() {
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F5, ActionEvent.ALT_MASK));
        putValue(SMALL_ICON, scaleIcon(loadIcon("/images/icon_zoomtofit.png"), 32, 32));
        putValue(NAME, "Zoom to fit");
        putValue(SHORT_DESCRIPTION, "Zoom to fit");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        MainFrame.getInstance().getPackageView().startZoomToFitState();
    }
}

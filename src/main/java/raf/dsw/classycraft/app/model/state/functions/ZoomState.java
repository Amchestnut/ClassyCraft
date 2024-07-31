package raf.dsw.classycraft.app.model.state.functions;

import raf.dsw.classycraft.app.controller.mouseAdapters.ZoomListener;
import raf.dsw.classycraft.app.gui.swing.view.DiagramView;
import raf.dsw.classycraft.app.model.state.State;

import java.awt.*;

public class ZoomState implements State {
    @Override
    public void misKliknut(int x, int y, DiagramView diagramView) {
        diagramView.addMouseWheelListener(new ZoomListener(diagramView, new Point(x,y)));
    }

    @Override
    public void misOtpusten(int x, int y, DiagramView diagramView) {
        diagramView.removeMouseWheelListener(diagramView.getMouseWheelListeners()[0]);
    }

    @Override
    public void misPrevucen(int x, int y, DiagramView diagramView) {

    }
}

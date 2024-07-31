package raf.dsw.classycraft.app.model.state;

import raf.dsw.classycraft.app.gui.swing.view.DiagramView;

public interface State {
    public void misKliknut(int x, int y, DiagramView diagramView);
    public void misOtpusten(int x, int y, DiagramView diagramView);
    public void misPrevucen(int x, int y, DiagramView diagramView);
}

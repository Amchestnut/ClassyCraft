package raf.dsw.classycraft.app.controller.mouseAdapters;

import raf.dsw.classycraft.app.gui.swing.view.DiagramView;
import raf.dsw.classycraft.app.gui.swing.view.MainFrame;

import java.awt.event.MouseEvent;

public class MouseAdapter extends java.awt.event.MouseAdapter {

    // If we click on DiagramView, it is registered here
    // We listen to clicks that were made on the DiagramView`
    private DiagramView diagramView;

    public MouseAdapter(DiagramView diagramView) {
        this.diagramView = diagramView;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        MainFrame.getInstance().getPackageView().misJeKliknut(e.getX(), e.getY(), diagramView);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        MainFrame.getInstance().getPackageView().misJeOtpusten(e.getX(), e.getY(), diagramView);
    }

    public void mouseDragged(MouseEvent e){
        MainFrame.getInstance().getPackageView().misJePrevucen(e.getX(), e.getY(), diagramView);
    }

    public DiagramView getDiagramView() {
        return diagramView;
    }

    public void setDiagramView(DiagramView diagramView) {
        this.diagramView = diagramView;
    }
}

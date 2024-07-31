package raf.dsw.classycraft.app.controller.mouseAdapters;

import raf.dsw.classycraft.app.gui.swing.view.DiagramView;
import raf.dsw.classycraft.app.gui.swing.view.MainFrame;

import java.awt.event.MouseEvent;

/*  MouseAdapter koriscen jer ako bi koristio MouseListener morao bih da implementiram svih 8 metoda

    *** Ako kliknemo na DIAGRAMVIEW, OVDE SE TO REGISTRUJE
    - Vidi u klasi MouseAdapter koje sve metode jos imaju i kako da ih koristis
    - ako slucajno negde koristim Mouseadapter.getDiagramView moguce da sam preksrsio mvc
 */
public class MouseAdapter extends java.awt.event.MouseAdapter {                 // ovde cuvam DIAGRAM VIEW

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

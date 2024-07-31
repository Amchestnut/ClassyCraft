package raf.dsw.classycraft.app.controller.mouseAdapters;

import raf.dsw.classycraft.app.gui.swing.view.DiagramView;

import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;

public class ZoomListener implements MouseWheelListener {
    private static final double SCALE_FACTOR = 1.1;
    private final DiagramView diagramView;
    private final Point point;

    public ZoomListener(DiagramView diagramView, Point point) {
        this.diagramView = diagramView;
        this.point = point;
    }

    public static void applyZoomToPanel(DiagramView diagramView, double scaleFactor, Point zoomPoint) {
        AffineTransform currentTransform = diagramView.getTransform();

        currentTransform.translate(zoomPoint.x, zoomPoint.y);
        currentTransform.scale(scaleFactor, scaleFactor);
        currentTransform.translate(-zoomPoint.x, -zoomPoint.y);

        diagramView.setTransform(currentTransform);
        diagramView.repaint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int counterMovement = e.getWheelRotation();
        double scalingFaktor = 0;
        if(counterMovement < 0){
            scalingFaktor = SCALE_FACTOR;
        }
        else{
            scalingFaktor = 1.0 / SCALE_FACTOR;
        }
        applyZoom(diagramView, scalingFaktor, point);
    }

    private void applyZoom(DiagramView diagramView, double scaleFactor, Point pointForZoom) {
        applyZoomToPanel(diagramView, scaleFactor, pointForZoom);
    }
}

package raf.dsw.classycraft.app.model.state.functions;

import raf.dsw.classycraft.app.controller.mouseAdapters.ZoomListener;
import raf.dsw.classycraft.app.gui.swing.painters.ElementPainter;
import raf.dsw.classycraft.app.gui.swing.painters.elementi.InterclassPainter;
import raf.dsw.classycraft.app.gui.swing.view.DiagramView;
import raf.dsw.classycraft.app.gui.swing.view.MainFrame;
import raf.dsw.classycraft.app.model.diagramElements.DiagramElement;
import raf.dsw.classycraft.app.model.diagramElements.elements.Interclass;
import raf.dsw.classycraft.app.model.state.State;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.List;

public class ZoomToFitState implements State {
    @Override
    public void misKliknut(int x, int y, DiagramView diagramView) {
        List<DiagramElement> diagramElements = getDiagramElements(diagramView);

        if(diagramElements.size()==1 && diagramElements.get(0) instanceof Interclass){
            MainFrame.getInstance().getPackageView().getScrollPane().getViewport().setViewPosition(applyTransformation(((Interclass)diagramElements.get(0)).getLocation(), diagramView));
        }
        else{
            Point topLeft = topLeftPoint(diagramElements);
            diagramView.setTransform(new AffineTransform());           // vrati na original, pocetno

            double randomDistanca = 600;                               // postavljeno je na osnovu random odabira, lepo ga zoomuje ovako
            double scalingFactor = howMuchScaleFactor(diagramView, randomDistanca);                             // scaling faktori xa x i y ose

            ZoomListener.applyZoomToPanel(diagramView, scalingFactor, applyTransformation(topLeft, diagramView)); // Iskoristi zoom faktor, pocetna tacka je gornji levi ugao
            MainFrame.getInstance().getPackageView().getScrollPane().getViewport().setViewPosition(applyTransformation(topLeft, diagramView));
        }
    }
    @Override
    public void misOtpusten(int x, int y, DiagramView diagramView) {

    }

    @Override
    public void misPrevucen(int x, int y, DiagramView diagramView) {

    }

    private Point applyTransformation(Point originalPoint, DiagramView paintedPanel) {
        AffineTransform transform = paintedPanel.getTransform();
        Point newPoint = new Point();

        try {
            transform.inverseTransform(originalPoint, newPoint);
        }
        catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }
        return newPoint;
    }
    private List<DiagramElement> getDiagramElements(DiagramView diagramView) {
        List<DiagramElement> diagramElements = new ArrayList<>();

        for (ElementPainter elementPainter : diagramView.getPainters()) {
            if(elementPainter instanceof InterclassPainter){
                diagramElements.add(elementPainter.getDiagramElement());
            }
        }
        return diagramElements;
    }
    private Point topLeftPoint(List<DiagramElement> diagramElements) {
        Point minPoint = new Point();
        Point maxPoint = new Point();

        koordinateZaPravougaonik(diagramElements, minPoint, maxPoint);          /// moramo da bi setovali minPoint i maxPoint
        return new Point(minPoint.x, minPoint.y);
    }
    private double howMuchScaleFactor(DiagramView diagramView, double specifiedDistance) {
        Rectangle boundingBox = granicePravougaonika(diagramView);

        // scaling faktori za x i y, na osnovu distance
        double scaleX = specifiedDistance / boundingBox.getWidth();
        double scaleY = specifiedDistance / boundingBox.getHeight();

        return Math.min(scaleX, scaleY);
    }
    private Rectangle granicePravougaonika(DiagramView diagramView) {
        List<DiagramElement> diagramElements = getDiagramElements(diagramView);

        Point minPoint = new Point();
        Point maxPoint = new Point();
        koordinateZaPravougaonik(diagramElements, minPoint, maxPoint);

        int width = maxPoint.x - minPoint.x;
        int height = maxPoint.y - minPoint.y;

        // vrati granice tog pravougaonika
        return new Rectangle(minPoint.x, minPoint.y, width, height);
    }

    private void koordinateZaPravougaonik(List<DiagramElement> diagramElements, Point minPoint, Point maxPoint) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (DiagramElement de : diagramElements) {
            Point location = ((Interclass) de).getLocation();
            minX = Math.min(minX, location.x);
            minY = Math.min(minY, location.y);
            maxX = Math.max(maxX, location.x);
            maxY = Math.max(maxY, location.y);
        }

        minPoint.setLocation(minX, minY);
        maxPoint.setLocation(maxX, maxY);
    }
}

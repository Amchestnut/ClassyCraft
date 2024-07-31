package raf.dsw.classycraft.app.gui.swing.painters.veze;

import raf.dsw.classycraft.app.gui.swing.painters.ElementPainter;
import raf.dsw.classycraft.app.model.diagramElements.DiagramElement;
import raf.dsw.classycraft.app.model.diagramElements.connections.*;
import raf.dsw.classycraft.app.model.diagramElements.elements.Interclass;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class ConnectionPainter extends ElementPainter {

    private DiagramElement interclassOD;
    private DiagramElement interclassDO;
    private Line2D myConnection = null;
    private DiagramElement diagramElement;
    private Rectangle2D rectangleAroundConnection;

    public ConnectionPainter(DiagramElement interclassOD, DiagramElement interclassDO, DiagramElement diagramElement) {
        this.interclassOD = interclassOD;
        this.interclassDO = interclassDO;
        this.diagramElement = diagramElement;
    }

    @Override
    public void paint(Graphics2D g) {

        double minDistance = Double.MAX_VALUE;

        Point first = null;
        Point second = null;

        if(interclassOD instanceof Interclass && interclassDO instanceof Interclass){
            if(((Interclass) interclassOD).getKonekcioneTacke().isEmpty())
                ((Interclass) interclassOD).setKonekcioneTacke(interclassOD);
            if(((Interclass) interclassDO).getKonekcioneTacke().isEmpty())
                ((Interclass) interclassDO).setKonekcioneTacke(interclassDO);

            for(Point p1 : ((Interclass) interclassOD).getKonekcioneTacke()){
                for(Point p2 : ((Interclass) interclassDO).getKonekcioneTacke()){
                    double distance = p1.distance(p2);
                    if(distance < minDistance){
                        minDistance = distance;
                        first = p1;
                        second = p2;
                    }
                }
            }
        }

        if (first != null && second != null) {
            myConnection = new Line2D.Double(first, second);
            if(diagramElement instanceof Connection){
                ((Connection)diagramElement).setKonekcionaTackaOD(first);
                ((Connection)diagramElement).setKonekcionaTackaDO(second);
            }

            // ivice i velicina invisible pravougaonika
            double x = Math.min(first.x, second.x);
            double y = Math.min(first.y, second.y);
            double width = Math.abs(first.x - second.x);
            double height = Math.abs(first.y - second.y);

            rectangleAroundConnection = new Rectangle2D.Double(x, y, width, height);
            Color transparentColor = new Color(0, 0, 0, 0); // Red, Green, Blue, Alpha
            g.setColor(transparentColor);


            g.draw(rectangleAroundConnection);


            if (diagramElement instanceof Asocijacija) {
                drawAssociationConnection(g);
            }
            else if (diagramElement instanceof Nasledjivanje) {
                drawInheritanceConnection(g, first, second);
            }
            else if (diagramElement instanceof Realizacija) {
                drawRealisationConnection(g, first, second);
            }
            if (diagramElement instanceof Zavisnost) {
                drawDependencyConnection(g, first, second);
            }
            else if (diagramElement instanceof Agregacija) {
                drawAggregationConnection(g, first, second);
            }
            else if (diagramElement instanceof Kompozicija) {
                drawCompositionConnection(g, first, second);
            }


        }
    }

    private void drawAssociationConnection(Graphics2D g){
        int colorCode = diagramElement.getColor();
        Color color = new Color(colorCode, true);           // argb color

        g.setColor(color);
        g.setStroke(new BasicStroke(3));
        g.draw(myConnection);
    }

    private void drawInheritanceConnection(Graphics2D g, Point first, Point second) {
        int colorCode = diagramElement.getColor();
        Color color = new Color(colorCode, true);

        g.setColor(color);
        g.setStroke(new BasicStroke(3));
        g.draw(myConnection);

        double offs = 20 * Math.PI / 180.0;
        double angle = Math.atan2(second.y - first.y, second.x - first.x);

        int[] xPoints = {
                second.x,
                second.x - (int) (20 * Math.cos(angle + offs)),
                second.x - (int) (20 * Math.cos(angle - offs))
        };
        int[] yPoints = {
                second.y,
                second.y - (int) (20 * Math.sin(angle + offs)),
                second.y - (int) (20 * Math.sin(angle - offs))
        };

        g.setColor(Color.BLACK);
        g.drawPolygon(xPoints, yPoints, 3);
        g.setColor(Color.WHITE);
        g.fillPolygon(xPoints, yPoints, 3);
    }

    private void drawRealisationConnection(Graphics2D g, Point first, Point second) {

        int colorCode = diagramElement.getColor();
        Color color = new Color(colorCode, true);

        g.setColor(color);
        float[] dash1 = {10.0f};
        BasicStroke dashed =
                new BasicStroke(3.0f,
                        BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER,
                        10.0f, dash1, 0.0f);
        g.setStroke(dashed);

        g.setColor(color);
        g.draw(myConnection);


        double offs = 20 * Math.PI / 180.0;
        double angle = Math.atan2(second.y - first.y, second.x - first.x);

        int[] xPoints = {
                second.x,
                second.x - (int) (20 * Math.cos(angle + offs)),
                second.x - (int) (20 * Math.cos(angle - offs))
        };
        int[] yPoints = {
                second.y,
                second.y - (int) (20 * Math.sin(angle + offs)),
                second.y - (int) (20 * Math.sin(angle - offs))
        };

        g.setColor(Color.BLACK);
        g.fillPolygon(xPoints, yPoints, 3);
    }

    private void drawDependencyConnection(Graphics2D g, Point first, Point second) {

        int colorCode = diagramElement.getColor();
        Color color = new Color(colorCode, true);

        g.setColor(color);
        float[] dash1 = {10.0f};
        BasicStroke dashed =
                new BasicStroke(3.0f,
                        BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER,
                        10.0f, dash1, 0.0f);
        g.setStroke(dashed);

        g.setColor(color);
        g.draw(myConnection);


        double angle = Math.atan2(second.y - first.y, second.x - first.x);
        double arrowAngleOffset = 20 * Math.PI / 180.0;

        int arrowLength = 20;

        Point arrowSide1 = new Point(
                (int) (second.x - arrowLength * Math.cos(angle + arrowAngleOffset)),
                (int) (second.y - arrowLength * Math.sin(angle + arrowAngleOffset))
        );
        Point arrowSide2 = new Point(
                (int) (second.x - arrowLength * Math.cos(angle - arrowAngleOffset)),
                (int) (second.y - arrowLength * Math.sin(angle - arrowAngleOffset))
        );


        g.setColor(Color.BLACK);

        g.setStroke(new BasicStroke(3));
        g.drawLine(arrowSide1.x, arrowSide1.y, second.x, second.y);
        g.drawLine(arrowSide2.x, arrowSide2.y, second.x, second.y);
    }

    private void drawAggregationConnection(Graphics2D g, Point from, Point to) {
        double angle = Math.atan2(to.y - from.y, to.x - from.x);
        double arrowAngleOffset = 20 * Math.PI / 180.0;

        int arrowLength = 20;

        Point arrowSide1 = new Point(
                (int) (to.x - arrowLength * Math.cos(angle + arrowAngleOffset)),
                (int) (to.y - arrowLength * Math.sin(angle + arrowAngleOffset))
        );
        Point arrowSide2 = new Point(
                (int) (to.x - arrowLength * Math.cos(angle - arrowAngleOffset)),
                (int) (to.y - arrowLength * Math.sin(angle - arrowAngleOffset))
        );


        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(3));
        g.drawLine(arrowSide1.x, arrowSide1.y, to.x, to.y);         // prva linija strelice
        g.drawLine(arrowSide2.x, arrowSide2.y, to.x, to.y);         // druga linija strelice

        g.setStroke(new BasicStroke(3));

        double drugiUgao = Math.atan2(from.y - to.y, from.x - to.x);

        int offsetDistance = 16;
        Point newTo = new Point(
                (int) (from.x - offsetDistance * Math.cos(drugiUgao)),
                (int) (from.y - offsetDistance * Math.sin(drugiUgao))
        );

        int diamondLength = 8;
        int diamondWidth = diamondLength * 2;

        Point diamondLeft = new Point(
                (int) (newTo.x + diamondLength * Math.cos(drugiUgao + Math.PI / 2)),
                (int) (newTo.y + diamondLength * Math.sin(drugiUgao + Math.PI / 2))
        );
        Point diamondRight = new Point(
                (int) (newTo.x + diamondLength * Math.cos(drugiUgao - Math.PI / 2)),
                (int) (newTo.y + diamondLength * Math.sin(drugiUgao - Math.PI / 2))
        );
        Point diamondTip = new Point(
                (int) (newTo.x + diamondWidth * Math.cos(drugiUgao)),
                (int) (newTo.y + diamondWidth * Math.sin(drugiUgao))
        );
        Point diamondBase = new Point(
                (int) (newTo.x - diamondWidth * Math.cos(drugiUgao)),
                (int) (newTo.y - diamondWidth * Math.sin(drugiUgao))
        );

        Polygon diamond = new Polygon();
        diamond.addPoint(diamondBase.x, diamondBase.y);
        diamond.addPoint(diamondLeft.x, diamondLeft.y);
        diamond.addPoint(diamondTip.x, diamondTip.y);
        diamond.addPoint(diamondRight.x, diamondRight.y);

        g.setColor(Color.WHITE);
        g.fillPolygon(diamond);

        g.setColor(Color.BLACK);
        g.drawPolygon(diamond);

        int colorCode = diagramElement.getColor();
        Color bojica = new Color(colorCode, true);
        g.setColor(bojica);
        g.drawLine(diamondBase.x, diamondBase.y, to.x, to.y);
    }

    private void drawCompositionConnection(Graphics2D g, Point from, Point to){
        double angle = Math.atan2(to.y - from.y, to.x - from.x);
        double arrowAngleOffset = 20 * Math.PI / 180.0;

        int arrowLength = 20;

        Point arrowSide1 = new Point(
                (int) (to.x - arrowLength * Math.cos(angle + arrowAngleOffset)),
                (int) (to.y - arrowLength * Math.sin(angle + arrowAngleOffset))
        );
        Point arrowSide2 = new Point(
                (int) (to.x - arrowLength * Math.cos(angle - arrowAngleOffset)),
                (int) (to.y - arrowLength * Math.sin(angle - arrowAngleOffset))
        );


        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(3));
        g.drawLine(arrowSide1.x, arrowSide1.y, to.x, to.y);         // prva linija strelice
        g.drawLine(arrowSide2.x, arrowSide2.y, to.x, to.y);         // druga linija strelice

        // sada radimo dijamant
        g.setStroke(new BasicStroke(3));

        // direkcija linija
        double drugiUgao = Math.atan2(from.y - to.y, from.x - to.x);

        // pomerim 'to' blize za 16 pixela
        int offsetDistance = 16;
        Point newTo = new Point(
                (int) (from.x - offsetDistance * Math.cos(drugiUgao)),
                (int) (from.y - offsetDistance * Math.sin(drugiUgao))
        );

        // velicina dijamanta
        int diamondLength = 8; // poluprecnik
        int diamondWidth = diamondLength * 2; // precnik

        Point diamondLeft = new Point(
                (int) (newTo.x + diamondLength * Math.cos(drugiUgao + Math.PI / 2)),
                (int) (newTo.y + diamondLength * Math.sin(drugiUgao + Math.PI / 2))
        );
        Point diamondRight = new Point(
                (int) (newTo.x + diamondLength * Math.cos(drugiUgao - Math.PI / 2)),
                (int) (newTo.y + diamondLength * Math.sin(drugiUgao - Math.PI / 2))
        );
        Point diamondTip = new Point(
                (int) (newTo.x + diamondWidth * Math.cos(drugiUgao)),
                (int) (newTo.y + diamondWidth * Math.sin(drugiUgao))
        );
        Point diamondBase = new Point(
                (int) (newTo.x - diamondWidth * Math.cos(drugiUgao)),
                (int) (newTo.y - diamondWidth * Math.sin(drugiUgao))
        );

        // ovde ga crtam, tj tacke
        Polygon diamond = new Polygon();
        diamond.addPoint(diamondBase.x, diamondBase.y);
        diamond.addPoint(diamondLeft.x, diamondLeft.y);
        diamond.addPoint(diamondTip.x, diamondTip.y);
        diamond.addPoint(diamondRight.x, diamondRight.y);

        // boja popuni
        g.setColor(Color.BLACK);
        g.fillPolygon(diamond);

        // crtaj od , do
        int colorCode = diagramElement.getColor();
        Color bojica = new Color(colorCode, true);
        g.setColor(bojica);
        g.drawLine(diamondBase.x, diamondBase.y, to.x, to.y);
    }


    @Override
    public boolean elementAt(DiagramElement diagramElement, Point location) {          // da li se nalazi neki element na TOJ TACKI
        return getRectangleAroundConnection().contains(location);
    }

    public Line2D getMyConnection() {
        return myConnection;
    }

    public void setMyConnection(Line2D myConnection) {
        this.myConnection = myConnection;
    }

    public DiagramElement getInterclassOD() {
        return interclassOD;
    }

    public void setInterclassOD(DiagramElement interclassOD) {
        this.interclassOD = interclassOD;
    }

    public DiagramElement getInterclassDO() {
        return interclassDO;
    }

    public void setInterclassDO(DiagramElement interclassDO) {
        this.interclassDO = interclassDO;
    }

    public DiagramElement getDiagramElement() {
        return diagramElement;
    }

    public void setDiagramElement(DiagramElement diagramElement) {
        this.diagramElement = diagramElement;
    }

    public Rectangle2D getRectangleAroundConnection() {
        return rectangleAroundConnection;
    }

    public void setRectangleAroundConnection(Rectangle2D rectangleAroundConnection) {
        this.rectangleAroundConnection = rectangleAroundConnection;
    }
}

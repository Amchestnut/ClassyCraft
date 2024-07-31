package raf.dsw.classycraft.app.gui.swing.painters.elementi;

import raf.dsw.classycraft.app.gui.swing.painters.ElementPainter;
import raf.dsw.classycraft.app.model.diagramElements.DiagramElement;
import raf.dsw.classycraft.app.model.diagramElements.elements.Enum;
import raf.dsw.classycraft.app.model.diagramElements.elements.*;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

public class InterclassPainter extends ElementPainter {         /// nacrtaj me kako je model ima recept da me nacrta

    protected Shape shape;
    private DiagramElement diagramElement;
    private List<Point> listOfConnectionDots = new ArrayList<>();

    public InterclassPainter(DiagramElement diagramElement){
        this.diagramElement = diagramElement;
    }
    @Override
    public void paint(Graphics2D g) {

        drawShape();                                // crtaj oblik za prosledjenu klasu. (takodje se trigeruje za move state)
        int strokeWidth = diagramElement.getStroke();
        Stroke stroke = new BasicStroke(strokeWidth);

        g.setColor(Color.BLACK);                    // sets the drawing color for the outline
        g.setStroke(stroke);                        // sets the stroke style for the outline
        g.draw(getShape());                         // draws the outline of the shape.

        int colorFromElement = 0;

        if(diagramElement instanceof Interclass){
            colorFromElement = diagramElement.getColor();
        }

        Color color = new Color(colorFromElement, true);                        // 'true' if the color includes alpha
        g.setPaint(color);                          // sets the color inside

        g.fill(getShape());                         // fills the shape with the set color.

        int x = 0;
        int y = 0;
        if(diagramElement instanceof Interclass){
            x = ((Interclass)diagramElement).getLocation().x;
            y = ((Interclass)diagramElement).getLocation().y + 8;       // korekcija
        }

        int PADDING = 1;
        int LINE_HEIGHT = 15;
        int lineAfterNameY = y + LINE_HEIGHT - 9;


        // pisanje imena
        g.setColor(Color.BLACK);
        if(diagramElement instanceof Interclass){
            String name = diagramElement.getName();
            FontMetrics fm = g.getFontMetrics();
            int stringWidth = fm.stringWidth(name);
            int elementWidth = ((Interclass)diagramElement).getDimension().width;
            int centerX = x + (elementWidth - stringWidth) / 2;                     // centar pravougaonika
            g.drawString(name, centerX, y + PADDING);                            // pisi to ime u centar
        }

        int currentY = LINE_HEIGHT;
        int lineAfterAttributesY = 0;
        boolean containsAttribute = false;

        // pisanje atributa i metoda za klasu
        if(diagramElement instanceof Klasa){
            for (ClassContent attribute : ((Klasa)diagramElement).getAttributes()) {
                if(attribute instanceof Atribut){
                    String sb = attribute.getVidljivost() + ((Atribut) attribute).getAttributeName();
                    g.drawString(sb, x + PADDING, y+ currentY + PADDING);
                    currentY += LINE_HEIGHT;
                    containsAttribute = true;
                }
            }
            if(containsAttribute){
                lineAfterAttributesY = y + currentY - 9;        // za iscrtavanje linije, normalno kad ima sve atribute, kao i pre
            }
            else{
                currentY += 10;
                lineAfterAttributesY = y + currentY - 9;        // za iscrtavanje linije kad nema atributa, da ostane prazan prostor
            }

            for (ClassContent method : ((Klasa)diagramElement).getMethods()) {
                if(method instanceof Metoda){
                    String sb = method.getVidljivost() + ((Metoda) method).getMethodName();
                    g.drawString(sb, x + PADDING,y+ currentY + PADDING);
                    currentY += LINE_HEIGHT;
                }
            }
        }
        // pisanje atributa i metoda za interfejs
        if(diagramElement instanceof Interfejs){
            currentY += 10;
            lineAfterAttributesY = y + currentY - 9;        // za iscrtavanje linije
            for (ClassContent method : ((Interfejs)diagramElement).getMethods()) {
                if(method instanceof Metoda){
                    String sb = method.getVidljivost() + ((Metoda) method).getMethodName();
                    g.drawString(sb, x + PADDING,y+ currentY + PADDING);
                    currentY += LINE_HEIGHT;
                }
            }
        }
        // pisanje atributa i metoda za enum
        if(diagramElement instanceof Enum){
            for (ClassContent attribute : ((Enum)diagramElement).getAttributes()) {
                if(attribute instanceof Atribut){
                    String sb = attribute.getVidljivost() + ((Atribut) attribute).getAttributeName();
                    g.drawString(sb, x + PADDING, y+ currentY + PADDING);
                    currentY += LINE_HEIGHT;
                    containsAttribute = true;
                }
            }
            if(containsAttribute){
                lineAfterAttributesY = y + currentY - 9;        // za iscrtavanje linije, normalno kad ima sve atribute, kao i pre
            }
            else{
                currentY += 10;
                lineAfterAttributesY = y + currentY - 9;        // za iscrtavanje linije kad nema atributa, da ostane prazan prostor
            }
            for (ClassContent method : ((Enum)diagramElement).getMethods()) {
                if(method instanceof Metoda){
                    String sb = method.getVidljivost() + ((Metoda) method).getMethodName();
                    g.drawString(sb, x + PADDING,y+ currentY + PADDING);
                    currentY += LINE_HEIGHT;
                }
            }
        }
        // pisanje atributa i metoda za apstrakt class
        if(diagramElement instanceof ApstraktnaKlasa){
            for (ClassContent attribute : ((ApstraktnaKlasa)diagramElement).getAttributes()) {
                if(attribute instanceof Atribut){
                    String sb = attribute.getVidljivost() + ((Atribut) attribute).getAttributeName();
                    g.drawString(sb, x + PADDING, y+ currentY + PADDING);
                    currentY += LINE_HEIGHT;
                    containsAttribute = true;
                }
            }
            if(containsAttribute){
                lineAfterAttributesY = y + currentY - 9;        // za iscrtavanje linije, normalno kad ima sve atribute, kao i pre
            }
            else{
                currentY += 10;
                lineAfterAttributesY = y + currentY - 9;        // za iscrtavanje linije kad nema atributa, da ostane prazan prostor
            }
            for (ClassContent method : ((ApstraktnaKlasa)diagramElement).getMethods()) {
                if(method instanceof Metoda){
                    String sb = method.getVidljivost() + ((Metoda) method).getMethodName();
                    g.drawString(sb, x + PADDING,y+ currentY + PADDING);
                    currentY += LINE_HEIGHT;
                }
            }
        }

        // Drawing the horizontal line
        Dimension dimension = ((Interclass)diagramElement).getDimension();

        g.setColor(Color.BLACK); // Line color
        g.drawLine(x, lineAfterNameY, x + dimension.width, lineAfterNameY);                    // line after name
        g.drawLine(x, lineAfterAttributesY, x + dimension.width, lineAfterAttributesY);         // line after attributes
    }

    @Override
    public boolean elementAt(DiagramElement diagramElement, Point location) {          // da li se nalazi neki element na TOJ TACKI
        return getShape().contains(location);
    }

    private void drawShape() {
        // Get the current location and dimension from the associated DiagramElement
        Interclass interclass = (Interclass) getDiagramElement();
        Point currentLocation = interclass.getLocation();
        Dimension dimension = interclass.getDimension();

        // updajteujem shape koordinate
        shape = new GeneralPath();
        ((GeneralPath)shape).moveTo(currentLocation.x, currentLocation.y);
        ((GeneralPath)shape).lineTo(currentLocation.x + dimension.width, currentLocation.y);
        ((GeneralPath)shape).lineTo(currentLocation.x + dimension.width, currentLocation.y + dimension.height);
        ((GeneralPath)shape).lineTo(currentLocation.x, currentLocation.y + dimension.height);
        ((GeneralPath)shape).closePath();
    }

    public Shape getShape() {
        if(shape == null)
            shape = new GeneralPath();
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public DiagramElement getDiagramElement() {
        return diagramElement;
    }

    public void setDiagramElement(DiagramElement diagramElement) {
        this.diagramElement = diagramElement;
    }

    public List<Point> getListOfConnectionDots() {
        return listOfConnectionDots;
    }

    public void setListOfConnectionDots(List<Point> listOfConnectionDots) {
        this.listOfConnectionDots = listOfConnectionDots;
    }
}
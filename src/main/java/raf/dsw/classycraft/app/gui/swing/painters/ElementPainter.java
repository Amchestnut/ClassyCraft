package raf.dsw.classycraft.app.gui.swing.painters;

import raf.dsw.classycraft.app.model.diagramElements.DiagramElement;

import java.awt.*;

public abstract class ElementPainter {
    private DiagramElement diagramElement;
    public ElementPainter() {
    }
    public abstract void paint (Graphics2D g);
    public abstract boolean elementAt (DiagramElement element, Point location);
    public abstract DiagramElement getDiagramElement();

}

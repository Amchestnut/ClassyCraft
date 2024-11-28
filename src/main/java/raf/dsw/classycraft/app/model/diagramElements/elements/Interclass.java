package raf.dsw.classycraft.app.model.diagramElements.elements;

import com.fasterxml.jackson.annotation.JsonIgnore;
import raf.dsw.classycraft.app.model.diagramElements.DiagramElement;
import raf.dsw.classycraft.app.model.diagramElements.connections.Connection;
import raf.dsw.classycraft.app.notifications.NotificationForAddingAndRemoving;
import raf.dsw.classycraft.app.notifications.NotificationForChangingDimensions;
import raf.dsw.classycraft.app.notifications.NotificationForChangingLocation;
import raf.dsw.classycraft.app.notifications.NotificationForChangingVisibility;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Interclass extends DiagramElement {
    private Point location;
    private Dimension dimension;
    @JsonIgnore
    private List<Connection> allConnectionsOnThisInterclass = new ArrayList<>();
    @JsonIgnore
    private List<Point> connectionPoints = new ArrayList<>();

    public Interclass(int color, int stroke, String name, Point location, Dimension dimension) {
        super(color, stroke, name);
        this.location = location;
        this.dimension = dimension;
    }
    public Interclass(){  // For jackson

    }
    public abstract String export();
    public abstract String ispisiMetode();
    public List<Connection> getAllConnectionsOnThisInterclass() {
        return allConnectionsOnThisInterclass;
    }

    public void setAllConnectionsOnThisInterclass(List<Connection> allConnectionsOnThisInterclass) {
        this.allConnectionsOnThisInterclass = allConnectionsOnThisInterclass;
    }

    public void addAConnectionInThisInterclass(Connection veza){
        allConnectionsOnThisInterclass.add(veza);
    }

    public void removeAConnectionInThisInterclass(Connection veza){
        allConnectionsOnThisInterclass.remove(veza);
        notifySubscribers(new NotificationForAddingAndRemoving("remove connection", veza));
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
        notifySubscribers(new NotificationForChangingLocation("lokacija", this));
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
        notifySubscribers(new NotificationForChangingDimensions("dimenzija", this));
        setConnectionPoints(this);
    }

    public List<Point> getConnectionsPoints() {
        return connectionPoints;
    }

    public void setConnectionPoints(DiagramElement diagramElement) {
        int width = 0;
        int height = 0;

        int x = 0;
        int y = 0;

        List<Point> listOfConnectionDots = new ArrayList<>();
        if(diagramElement instanceof Interclass){
            x = ((Interclass)diagramElement).getLocation().x;
            y = ((Interclass)diagramElement).getLocation().y;
            width = ((Interclass)diagramElement).getDimension().width;
            height = ((Interclass)diagramElement).getDimension().height;
        }

        Point midTop = new Point(x + width / 2, y);
        Point midRight = new Point(x + width, y + height / 2);
        Point midBottom = new Point(x + width / 2, y + height);
        Point midLeft = new Point(x, y + height / 2);

        listOfConnectionDots.add(midTop);
        listOfConnectionDots.add(midRight);
        listOfConnectionDots.add(midBottom);
        listOfConnectionDots.add(midLeft);

        this.connectionPoints = listOfConnectionDots;
        notifySubscribers(new NotificationForChangingVisibility("novetacke", this)); // Could add a new notification for this specifically
    }
}

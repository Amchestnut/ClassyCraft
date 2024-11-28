package raf.dsw.classycraft.app.model.diagramElements.connections;

import raf.dsw.classycraft.app.model.diagramElements.DiagramElement;
import raf.dsw.classycraft.app.model.diagramElements.elements.Interclass;
import raf.dsw.classycraft.app.notifications.NotificationForChangingVisibility;

import java.awt.*;

public abstract class Connection extends DiagramElement {
    private String type;
    private String visibilityOfTheFirstElement = "";
    private String instanceOfTheFirstElement = "";
    private String cardinalityOfTheFirstElement = "";

    private String visibilityOfTheSecondElement = "";
    private String instanceOfTheSecondElement = "";
    private String cardinalityOfTheSecondElement = "";

    private Interclass interclassFROM;
    private Interclass interclassTO;

    private Point connectionPointFROM;
    private Point connectionPointTO;

    public Connection(int color, int stroke, String name, String type, Interclass interclassFROM, Interclass interclassTO) {
        super(color, stroke, name);
        this.type = type;
        this.interclassFROM = interclassFROM;
        this.interclassTO = interclassTO;
    }
    public Connection(){   // For jackson

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVisibilityOfTheFirstElement() {
        return visibilityOfTheFirstElement;
    }

    public void setVisibilityOfTheFirstElement(String visibilityOfTheFirstElement) {
        this.visibilityOfTheFirstElement = visibilityOfTheFirstElement;
    }

    public String getInstanceOfTheFirstElement() {
        return instanceOfTheFirstElement;
    }

    public void setInstanceOfTheFirstElement(String instanceOfTheFirstElement) {
        this.instanceOfTheFirstElement = instanceOfTheFirstElement;
    }

    public String getCardinalityOfTheFirstElement() {
        return cardinalityOfTheFirstElement;
    }

    public void setCardinalityOfTheFirstElement(String cardinalityOfTheFirstElement) {
        this.cardinalityOfTheFirstElement = cardinalityOfTheFirstElement;
    }

    public String getVisibilityOfTheSecondElement() {
        return visibilityOfTheSecondElement;
    }

    public void setVisibilityOfTheSecondElement(String visibilityOfTheSecondElement) {
        this.visibilityOfTheSecondElement = visibilityOfTheSecondElement;
    }

    public String getInstanceOfTheSecondElement() {
        return instanceOfTheSecondElement;
    }

    public void setInstanceOfTheSecondElement(String instanceOfTheSecondElement) {
        this.instanceOfTheSecondElement = instanceOfTheSecondElement;
    }

    public String getCardinalityOfTheSecondElement() {
        return cardinalityOfTheSecondElement;
    }

    public void setCardinalityOfTheSecondElement(String cardinalityOfTheSecondElement) {
        this.cardinalityOfTheSecondElement = cardinalityOfTheSecondElement;
    }

    public Interclass getInterclassFROM() {
        return interclassFROM;
    }

    public void setInterclassFROM(Interclass interclassFROM) {
        this.interclassFROM = interclassFROM;
    }

    public Interclass getInterclassTO() {
        return interclassTO;
    }

    public void setInterclassTO(Interclass interclassTO) {
        this.interclassTO = interclassTO;
    }

    public Point getConnectionPointFROM() {
        return connectionPointFROM;
    }

    public void setConnectionPointFROM(Point connectionPointFROM) {
        this.connectionPointFROM = connectionPointFROM;
        notifySubscribers(new NotificationForChangingVisibility("Connection point changed", this)); // Feel free to change this message, add a new one specifically if you want
    }

    public Point getConnectionPointTO() {
        return connectionPointTO;
    }

    public void setConnectionPointTO(Point connectionPointTO) {
        this.connectionPointTO = connectionPointTO;
        notifySubscribers(new NotificationForChangingVisibility("Connection point changed", this));
    }
}
package raf.dsw.classycraft.app.model.diagramElements.connections;

import raf.dsw.classycraft.app.model.diagramElements.DiagramElement;
import raf.dsw.classycraft.app.model.diagramElements.elements.Interclass;
import raf.dsw.classycraft.app.notifications.NotificationForChangingVisibility;

import java.awt.*;

public abstract class Connection extends DiagramElement {
    private String type;
    private String visibilityOfTheFirstElement = "";
    private String instanceOfTheFirstElement = "";
    private String kardinalnostOfTheFirstElement = "";

    private String visibilityOfTheSecondElement = "";
    private String instanceOfTheSecondElement = "";
    private String kardinalnostOfTheSecondElement = "";

    private Interclass interclassOD;
    private Interclass interclassDO;

    private Point konekcionaTackaOD;
    private Point konekcionaTackaDO;

    public Connection(int color, int stroke, String name, String type, Interclass interclassOD, Interclass interclassDO) {
        super(color, stroke, name);
        this.type = type;
        this.interclassOD = interclassOD;
        this.interclassDO = interclassDO;
    }
    public Connection(){    //za Json, nema drugu svrhu

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

    public String getKardinalnostOfTheFirstElement() {
        return kardinalnostOfTheFirstElement;
    }

    public void setKardinalnostOfTheFirstElement(String kardinalnostOfTheFirstElement) {
        this.kardinalnostOfTheFirstElement = kardinalnostOfTheFirstElement;
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

    public String getKardinalnostOfTheSecondElement() {
        return kardinalnostOfTheSecondElement;
    }

    public void setKardinalnostOfTheSecondElement(String kardinalnostOfTheSecondElement) {
        this.kardinalnostOfTheSecondElement = kardinalnostOfTheSecondElement;
    }

    public Interclass getInterclassOD() {
        return interclassOD;
    }

    public void setInterclassOD(Interclass interclassOD) {
        this.interclassOD = interclassOD;
    }

    public Interclass getInterclassDO() {
        return interclassDO;
    }

    public void setInterclassDO(Interclass interclassDO) {
        this.interclassDO = interclassDO;
    }

    public Point getKonekcionaTackaOD() {
        return konekcionaTackaOD;
    }
    public Point getKonekcionaTackaDO() {
        return konekcionaTackaDO;
    }
    public void setKonekcionaTackaOD(Point konekcionaTackaOD) {
        this.konekcionaTackaOD = konekcionaTackaOD;
        notifySubscribers(new NotificationForChangingVisibility("Promenjena konekciona tacka", this));      /// PROMENI NOTIFIKACIJU
    }
    public void setKonekcionaTackaDO(Point konekcionaTackaDO) {
        this.konekcionaTackaDO = konekcionaTackaDO;
        notifySubscribers(new NotificationForChangingVisibility("Promenjena konekciona tacka", this));      /// PROMENI NOTIFIKACIJU
    }
}
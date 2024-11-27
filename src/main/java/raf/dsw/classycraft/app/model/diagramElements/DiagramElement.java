package raf.dsw.classycraft.app.model.diagramElements;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import raf.dsw.classycraft.app.model.diagramElements.elements.Enum;
import raf.dsw.classycraft.app.model.diagramElements.elements.Interfejs;
import raf.dsw.classycraft.app.model.diagramElements.elements.Klasa;
import raf.dsw.classycraft.app.model.repositoryModel.ClassyNode;
import raf.dsw.classycraft.app.notifications.NotificationForChangedColor;
import raf.dsw.classycraft.app.observer.IPublisher;
import raf.dsw.classycraft.app.observer.ISubscriber;

import java.util.ArrayList;
import java.util.List;

public abstract class DiagramElement extends ClassyNode implements IPublisher, Cloneable {
    private int stroke;
    private String name;
    private List<ISubscriber> subs = new ArrayList<>();
    private int color;
    public DiagramElement(int color, int stroke, String name) {
        this.color = color;
        this.stroke = stroke;
        this.name = name;
    }
    public DiagramElement(){    // for jackson

    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        notifySubscribers(new NotificationForChangedColor("boja", this));
    }

    public int getStroke() {
        return stroke;
    }

    public void setStroke(int stroke) {
        this.stroke = stroke;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
/// u add child, kad dodam metodu, da preracunam ponovo sirinu i visinu i notify subskrajbere
    @Override
    public void addSubscriber(ISubscriber iSubscriber) {
        this.subs.add(iSubscriber);
    }

    @Override
    public void removeSubscriber(ISubscriber iSubscriber) {
        this.subs.remove(iSubscriber);
    }

    @Override
    public void notifySubscribers(Object notification) {
        for(ISubscriber subscriber : subs){
            subscriber.update(notification);
        }
    }

    @Override
    public DiagramElement clone() {
        try {
            DiagramElement clone = (DiagramElement) super.clone();
            // deep copy a ne shallow copy
            clone.subs = new ArrayList<>(this.subs);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

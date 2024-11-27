package raf.dsw.classycraft.app.model.modelImplementation;

import com.fasterxml.jackson.annotation.JsonTypeName;
import raf.dsw.classycraft.app.model.repositoryModel.ClassyNode;
import raf.dsw.classycraft.app.model.repositoryModel.ClassyNodeComposite;
import raf.dsw.classycraft.app.notifications.NotificationForAddingAndRemoving;
import raf.dsw.classycraft.app.notifications.NotificationForChangingName;
import raf.dsw.classycraft.app.observer.IPublisher;
import raf.dsw.classycraft.app.observer.ISubscriber;

import java.util.ArrayList;
import java.util.List;

@JsonTypeName("Diagram")
public class Diagram extends ClassyNodeComposite implements IPublisher {
    private String path;
    private transient List<ISubscriber> subs;
    public Diagram(String name, ClassyNode parent) {
        subs = new ArrayList<>();
        this.setName(name);
        this.setParent(parent);
    }
    public Diagram(){
        subs = new ArrayList<>();
    }
    public void removeFromParent(){
        if(getParent() instanceof ClassyNodeComposite){
            ((ClassyNodeComposite) getParent()).deleteChild(this);
        }
    }

    @Override
    public void setName(String name) {
        notifySubscribers(new NotificationForChangingName(name, this));
        super.setName(name);
    }
    @Override
    public void notifySubscribers(Object notification) {
        for(ISubscriber subscriber : subs){
            subscriber.update(notification);
        }
    }
    @Override
    public boolean addChild(ClassyNode classyNode) {
        notifySubscribers(new NotificationForAddingAndRemoving("ADD", classyNode));   // tehnicki nepotrebno, jer se painteri ubacuju direkt
        classyNode.setParent(this);
        return this.getChildren().add(classyNode);
    }

    @Override
    public void deleteChild(ClassyNode classyNode) {
        notifySubscribers(new NotificationForAddingAndRemoving("REMOVE", classyNode)); // izbrisana INTERKLASA iz modela, reci to view-u
        this.getChildren().remove(classyNode);
    }
    @Override
    public void addSubscriber(ISubscriber iSubscriber) {
        this.subs.add(iSubscriber);
    }

    @Override
    public void removeSubscriber(ISubscriber iSubscriber) {
        this.subs.remove(iSubscriber);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

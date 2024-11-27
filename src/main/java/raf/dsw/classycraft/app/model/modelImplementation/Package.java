package raf.dsw.classycraft.app.model.modelImplementation;

import com.fasterxml.jackson.annotation.JsonTypeName;
import raf.dsw.classycraft.app.model.repositoryModel.ClassyNode;
import raf.dsw.classycraft.app.model.repositoryModel.ClassyNodeComposite;
import raf.dsw.classycraft.app.notifications.NotificationForAddingAndRemoving;
import raf.dsw.classycraft.app.notifications.NotificationForChangingName;
import raf.dsw.classycraft.app.notifications.NotificationForOpeningPackage;
import raf.dsw.classycraft.app.observer.IPublisher;
import raf.dsw.classycraft.app.observer.ISubscriber;

import java.util.ArrayList;
import java.util.List;

@JsonTypeName("Package")
public class Package extends ClassyNodeComposite implements IPublisher {
    private String path;
    private transient List<ISubscriber> subs;

    public Package(String name, ClassyNode parent) {
        super.setName(name);
        this.setParent(parent);
        subs = new ArrayList<>();
    }
    public Package(){
        subs = new ArrayList<>();
    }
    public void openPackage(){
        notifySubscribers(new NotificationForOpeningPackage(this, null));
    }

    @Override
    public void setName(String name) {
        notifySubscribers(new NotificationForChangingName(name, this));
        super.setName(name);
    }
    @Override
    public boolean addChild(ClassyNode classyNode) {
        notifySubscribers(new NotificationForAddingAndRemoving("ADD", classyNode));
        classyNode.setParent(this);
        return this.getChildren().add(classyNode);
    }

    @Override
    public void deleteChild(ClassyNode classyNode) {
        notifySubscribers(new NotificationForAddingAndRemoving("REMOVE", classyNode));
        this.getChildren().remove(classyNode);
    }
    public void removeFromParent(){
        ClassyNode parent = this.getParent();
        if(parent instanceof ClassyNodeComposite){
            if(parent instanceof Package)
                ((Package) parent).deleteChild(this);
            else if(parent instanceof Project)
                ((Project) parent).deleteChild(this);
        }
    }

    @Override
    public void addSubscriber(ISubscriber iSubscriber) {
        subs.add(iSubscriber);
    }

    @Override
    public void removeSubscriber(ISubscriber iSubscriber) {
        subs.remove(iSubscriber);
    }

    @Override
    public void notifySubscribers(Object notification) {
        for(ISubscriber subscriber : subs){
            subscriber.update(notification);
        }
    }
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

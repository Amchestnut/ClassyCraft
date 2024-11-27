package raf.dsw.classycraft.app.model.modelImplementation;

import com.fasterxml.jackson.annotation.JsonTypeName;
import raf.dsw.classycraft.app.model.repositoryModel.ClassyNode;
import raf.dsw.classycraft.app.model.repositoryModel.ClassyNodeComposite;
import raf.dsw.classycraft.app.notifications.NotificationForAddingAndRemoving;
import raf.dsw.classycraft.app.notifications.NotificationForChangingAuthor;
import raf.dsw.classycraft.app.notifications.NotificationForChangingName;
import raf.dsw.classycraft.app.observer.IPublisher;
import raf.dsw.classycraft.app.observer.ISubscriber;

import java.util.ArrayList;
import java.util.List;

@JsonTypeName("Project")
public class Project extends ClassyNodeComposite implements IPublisher {
    private String author;
    private String path;
    private boolean changed;
    private transient List<ISubscriber> subs;
    public Project(String name, ClassyNode parent) {
        subs = new ArrayList<>();
        this.setName(name);
        this.setParent(parent);
        author = "Author1";
        changed = true;
    }
    public Project(){
        subs = new ArrayList<>();
    }
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        notifySubscribers(new NotificationForChangingAuthor(author, this));
        this.author = author;
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
        if(getParent() instanceof ClassyNodeComposite){
            ((ClassyNodeComposite) getParent()).getChildren().remove(this);
            notifySubscribers(new NotificationForAddingAndRemoving("REMOVE", this));
        }
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

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
}

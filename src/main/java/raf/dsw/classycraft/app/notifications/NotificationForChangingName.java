package raf.dsw.classycraft.app.notifications;

import raf.dsw.classycraft.app.model.repositoryModel.ClassyNode;

public class NotificationForChangingName {
    private String name;
    private ClassyNode node;

    public NotificationForChangingName(String name, ClassyNode node) {
        this.name = name;
        this.node = node;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClassyNode getNode() {
        return node;
    }

    public void setNode(ClassyNode node) {
        this.node = node;
    }
}

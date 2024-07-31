package raf.dsw.classycraft.app.notifications;

import raf.dsw.classycraft.app.model.repositoryModel.ClassyNode;

public class NotificationForChangingLocation {
    private String type;
    private ClassyNode node;

    public NotificationForChangingLocation(String type, ClassyNode node) {
        this.type = type;
        this.node = node;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ClassyNode getNode() {
        return node;
    }

    public void setNode(ClassyNode node) {
        this.node = node;
    }
}

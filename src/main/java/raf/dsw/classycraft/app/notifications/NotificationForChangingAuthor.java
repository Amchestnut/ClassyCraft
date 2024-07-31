package raf.dsw.classycraft.app.notifications;

import raf.dsw.classycraft.app.model.repositoryModel.ClassyNode;

public class NotificationForChangingAuthor {
    private String newAuthorName;
    private ClassyNode node;

    public NotificationForChangingAuthor(String newAuthorName, ClassyNode node) {
        this.newAuthorName = newAuthorName;
        this.node = node;
    }

    public String getNewAuthorName() {
        return newAuthorName;
    }

    public void setNewAuthorName(String newAuthorName) {
        this.newAuthorName = newAuthorName;
    }

    public ClassyNode getNode() {
        return node;
    }

    public void setNode(ClassyNode node) {
        this.node = node;
    }
}

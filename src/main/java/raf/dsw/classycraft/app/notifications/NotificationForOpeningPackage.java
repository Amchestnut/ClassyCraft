package raf.dsw.classycraft.app.notifications;

import raf.dsw.classycraft.app.model.repositoryModel.ClassyNode;

public class NotificationForOpeningPackage {
    private ClassyNode node;
    private ClassyNode loaded;

    public NotificationForOpeningPackage(ClassyNode node, ClassyNode loaded) {
        this.node = node;
        this.loaded = loaded;
    }

    public ClassyNode getLoaded() {
        return loaded;
    }

    public void setLoaded(ClassyNode loaded) {
        this.loaded = loaded;
    }

    public ClassyNode getNode() {
        return node;
    }

    public void setNode(ClassyNode node) {
        this.node = node;
    }
}

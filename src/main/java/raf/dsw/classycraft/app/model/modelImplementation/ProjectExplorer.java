package raf.dsw.classycraft.app.model.modelImplementation;

import raf.dsw.classycraft.app.model.repositoryModel.ClassyNode;
import raf.dsw.classycraft.app.model.repositoryModel.ClassyNodeComposite;

public class ProjectExplorer extends ClassyNodeComposite {
    public ProjectExplorer(String name) {
        this.setName(name);
    }

    @Override
    public boolean addChild(ClassyNode classyNode) {
        classyNode.setParent(this);
        return this.getChildren().add(classyNode);
    }
    @Override
    public void deleteChild(ClassyNode classyNode) {
        this.getChildren().remove(classyNode);
    }
}
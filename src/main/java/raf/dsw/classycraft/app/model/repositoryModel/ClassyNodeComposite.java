package raf.dsw.classycraft.app.model.repositoryModel;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import raf.dsw.classycraft.app.model.modelImplementation.Diagram;
import raf.dsw.classycraft.app.model.modelImplementation.Project;

import java.util.ArrayList;
import java.util.List;


public abstract class ClassyNodeComposite extends ClassyNode{
    private List<ClassyNode> children = new ArrayList<>();

    public abstract boolean addChild(ClassyNode classyNode);

    public abstract void deleteChild(ClassyNode classyNode);

    public List<ClassyNode> getChildren() {
        return children;
    }

    public void setChildren(List<ClassyNode> children) {
        this.children = children;
    }
}

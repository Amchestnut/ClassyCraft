package raf.dsw.classycraft.app.model.repositoryModel;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import raf.dsw.classycraft.app.model.modelImplementation.Diagram;
import raf.dsw.classycraft.app.model.modelImplementation.Project;

import java.util.ArrayList;
import java.util.List;

//@JsonTypeInfo(
//        use = JsonTypeInfo.Id.NAME,
//        include = JsonTypeInfo.As.PROPERTY,
//        property = "type")
//@JsonSubTypes({
//        @JsonSubTypes.Type(value = Project.class, name = "Project"),
//        @JsonSubTypes.Type(value = Package.class, name = "Package"),
//        @JsonSubTypes.Type(value = Diagram.class, name = "Diagram")
//})

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

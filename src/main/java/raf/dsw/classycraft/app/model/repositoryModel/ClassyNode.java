package raf.dsw.classycraft.app.model.repositoryModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import raf.dsw.classycraft.app.model.diagramElements.DiagramElement;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
//@JsonTypeInfo(
//        use = JsonTypeInfo.Id.NAME,
//        include = JsonTypeInfo.As.PROPERTY,
//        property = "type")
//@JsonSubTypes({
//        @JsonSubTypes.Type(value = Project.class, name = "Project"),
//        @JsonSubTypes.Type(value = Package.class, name = "Package"),
//        @JsonSubTypes.Type(value = Diagram.class, name = "Diagram")
//})

public abstract class ClassyNode {
    @JsonIgnore
    private ClassyNode parent;
    private String name;

    public ClassyNode getParent() {
        return parent;
    }

    public void setParent(ClassyNode parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

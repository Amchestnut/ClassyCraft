package raf.dsw.classycraft.app.model.diagramElements.elements;

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonTypeName("Enum")
public class Enum extends Interclass{
    private List<ClassContent> attributes;
    private List<ClassContent> methods;
    public Enum(int color, int stroke, String name, Point location, Dimension dimension) {
        super(color, stroke, name, location, dimension);
        attributes = new ArrayList<>();
        methods = new ArrayList<>();
    }
    public Enum(){ // For jackson
        attributes = new ArrayList<>();
        methods = new ArrayList<>();
    }

    @Override
    public String export() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("public enum " + getName() + "{\n");
        for(ClassContent classContent : attributes) {
            stringBuilder.append(classContent.export() + ",\n");
        }
        for(ClassContent classContent : methods){
            stringBuilder.append(classContent.export() + ",\n");
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    @Override
    public String ispisiMetode() {
        StringBuilder stringBuilder = new StringBuilder();
        for(ClassContent classContent : methods){
            String vidljivost = classContent.getVidljivost();
            if(Objects.equals(vidljivost, "+"))
                vidljivost = "public ";
            else if (Objects.equals(vidljivost, "-"))
                vidljivost = "private ";
            else if (vidljivost.equals("#"))
                vidljivost = "protected ";
            stringBuilder.append("\t").append(vidljivost).append(classContent.export()).append("\n");
        }
        return stringBuilder.toString();
    }

    public List<ClassContent> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<ClassContent> attributes) {
        this.attributes = attributes;
    }

    public List<ClassContent> getMethods() {
        return methods;
    }

    public void setMethods(List<ClassContent> methods) {
        this.methods = methods;
    }
}


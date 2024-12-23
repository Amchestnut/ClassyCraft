package raf.dsw.classycraft.app.model.diagramElements.elements;

import com.fasterxml.jackson.annotation.JsonTypeName;
import raf.dsw.classycraft.app.model.diagramElements.connections.Connection;
import raf.dsw.classycraft.app.model.diagramElements.connections.InheritanceConnection;
import raf.dsw.classycraft.app.model.diagramElements.connections.RealisationConnection;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonTypeName("AbstractClass")
public class AbstractClass extends Interclass{
    private List<ClassContent> attributes;
    private List<ClassContent> methods;

    public AbstractClass(int color, int stroke, String name, Point location, Dimension dimension) {       // guide how to draw
        super(color, stroke, name, location, dimension);
        attributes = new ArrayList<>();
        methods = new ArrayList<>();
    }
    public AbstractClass(){ // For jackson
        attributes = new ArrayList<>();
        methods = new ArrayList<>();
    }

    @Override
    public String export() {
        StringBuilder stringBuilder = new StringBuilder();
        String name = getName();

        String visibility = "";
        String tmp = "";
        if(name.contains("+")) {
            visibility = "public ";
            tmp = "\\+";
        }
        else if(name.contains("-")) {
            visibility = "private ";
            tmp = "-";
        }
        else if(name.contains("#")) {
            visibility = "protected ";
            tmp = "#";
        }

        String doIExtendOrImplementSomething = getAbstraction();

        stringBuilder.append(visibility + "abstract class " + getName().split(tmp)[1] + doIExtendOrImplementSomething + "{\n");
        for(ClassContent classContent : attributes){
            stringBuilder.append(classContent.export() + "\n");
        }
        for(ClassContent classContent : methods){
            stringBuilder.append(classContent.export() + "\n");
        }
        if(doIExtendOrImplementSomething != null){
            stringBuilder.append(writeAllAttributesAndMethodsOfTheInheritedClass());
        }
        stringBuilder.append("}\n");
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

    public String writeAllAttributesAndMethodsOfTheInheritedClass(){
        Interclass whoDoIInherit;
        for(Connection connection : getAllConnectionsOnThisInterclass()){
            if(connection.getInterclassFROM().equals(this)){
                whoDoIInherit = connection.getInterclassTO();
                return whoDoIInherit.ispisiMetode();
            }
        }
        return "";
    }

    public String getAbstraction(){
        StringBuilder stringBuilder = new StringBuilder();

        for(Connection connection : getAllConnectionsOnThisInterclass()){
            if(connection.getInterclassFROM().equals(this)){
                Interclass interclass = connection.getInterclassTO();
                if(connection instanceof RealisationConnection) {
                    stringBuilder.append(" implements ").append(interclass.getName());
                }
                else if(connection instanceof InheritanceConnection){
                    String tmp = String.valueOf(interclass.getName().charAt(0));
                    if(tmp.equals("+"))
                        tmp = "\\+";
                    stringBuilder.append(" extends ").append(interclass.getName().split(tmp)[1]);
                }
                break;
            }
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

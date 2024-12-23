package raf.dsw.classycraft.app.model.diagramElements.elements;

import com.fasterxml.jackson.annotation.JsonTypeName;
import raf.dsw.classycraft.app.model.diagramElements.connections.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonTypeName("Klasa")
public class Klasa extends Interclass{
    private List<ClassContent> attributes;
    private List<ClassContent> methods;

    public Klasa(int color, int stroke, String name, Point location, Dimension dimension) {       // guide how to draw
        super(color, stroke, name, location, dimension);
        attributes = new ArrayList<>();
        methods = new ArrayList<>();
    }
    public Klasa(){    // For jackson
        attributes = new ArrayList<>();
        methods = new ArrayList<>();
    }

    @Override
    public String ispisiMetode() {
        StringBuilder stringBuilder = new StringBuilder();
        for(ClassContent classContent : methods){
            String vidljivost = classContent.getVidljivost();
            if(Objects.equals(vidljivost, "+"))
                vidljivost = "public";
            else if (Objects.equals(vidljivost, "-"))
                vidljivost = "private";
            else if (vidljivost.equals("#"))
                vidljivost = "protected";
            stringBuilder.append("\t").append(vidljivost).append(classContent.export()).append("\n");
        }
        return stringBuilder.toString();
    }
    @Override
    public String export() {
        StringBuilder stringBuilder = new StringBuilder();
        String name = getName();

        String vidljiv = "";
        String tmp = "";
        if(name.contains("+")) {
            vidljiv = "public ";
            tmp = "\\+";
        }
        else if(name.contains("-")) {
            vidljiv = "private ";
            tmp = "-";
        }
        else if(name.contains("#")) {
            vidljiv = "protected ";
            tmp = "#";
        }
        String doIExtendOrImplementSomething = getAbstraction();

        stringBuilder.append(vidljiv).append("class ").append(getName().split(tmp)[1]).append(doIExtendOrImplementSomething).append("{\n");
        for(ClassContent classContent : attributes){
            stringBuilder.append("\t").append(classContent.export()).append("\n");
        }
        for(Connection connection : getAllConnectionsOnThisInterclass()){     // for the hidden
            if(connection instanceof DependencyConnection && connection.getInterclassFROM().equals(this)){
                Interclass druga = connection.getInterclassTO();

                String temp = String.valueOf(druga.getName().charAt(0));
                if(temp.equals("+"))
                    temp = "\\+";

                String vidljivost = "";
                String ime = druga.getName().split(temp)[1];
                switch (temp) {
                    case "\\+":
                        vidljivost = "public ";
                        break;
                    case "-":
                        vidljivost = "private ";
                        break;
                    case "#":
                        vidljivost = "protected ";
                        break;
                }

                stringBuilder.append("\t").append(vidljivost).append(ime).append(" ").append(ime.toLowerCase()).append(";\n");
            }
            if(connection instanceof AssociationConnection){
                Interclass druga;
                if(connection.getInterclassFROM().equals(this))
                    druga = connection.getInterclassTO();
                else
                    druga = connection.getInterclassFROM();

                String temp = String.valueOf(druga.getName().charAt(0));
                if(temp.equals("+"))
                    temp = "\\+";

                String vidljivost = "";
                String ime = druga.getName().split(temp)[1];
                switch (temp) {
                    case "\\+":
                        vidljivost = "public ";
                        break;
                    case "-":
                        vidljivost = "private ";
                        break;
                    case "#":
                        vidljivost = "protected ";
                        break;
                }

                stringBuilder.append("\t").append(vidljivost).append(ime).append(" ").append(ime.toLowerCase()).append(";\n");
            }
            if(connection instanceof AggregationConnection && connection.getInterclassFROM().equals(this)){
                Interclass druga = connection.getInterclassTO();

                String temp = String.valueOf(druga.getName().charAt(0));
                if(temp.equals("+"))
                    temp = "\\+";

                String vidljivost = "";
                String ime = druga.getName().split(temp)[1];
                switch (temp) {
                    case "\\+":
                        vidljivost = "public ";
                        break;
                    case "-":
                        vidljivost = "private ";
                        break;
                    case "#":
                        vidljivost = "protected ";
                        break;
                }

                stringBuilder.append("\t").append(vidljivost).append(ime).append(" ").append(ime.toLowerCase()).append(";\n");
            }
            if(connection instanceof CompositionConnection && connection.getInterclassFROM().equals(this)){
                Interclass druga = connection.getInterclassTO();

                String temp = String.valueOf(druga.getName().charAt(0));
                if(temp.equals("+"))
                    temp = "\\+";

                String vidljivost = "";
                String ime = druga.getName().split(temp)[1];
                switch (temp) {
                    case "\\+":
                        vidljivost = "public ";
                        break;
                    case "-":
                        vidljivost = "private ";
                        break;
                    case "#":
                        vidljivost = "protected ";
                        break;
                }

                stringBuilder.append("\t").append(vidljivost).append(ime).append(" ").append(ime.toLowerCase()).append(";\n");
            }
        }
        for(ClassContent classContent : methods){
            stringBuilder.append("\t").append(classContent.export()).append("\n");
        }
        if(doIExtendOrImplementSomething != null){
            stringBuilder.append(writeTheAttributesAndMethodsOfTheInheritedClass());
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
    public String writeTheAttributesAndMethodsOfTheInheritedClass(){
        Interclass whatDoIInherit;
        for(Connection connection : getAllConnectionsOnThisInterclass()){
            if(connection.getInterclassFROM().equals(this)){
                whatDoIInherit = connection.getInterclassTO();
                return whatDoIInherit.ispisiMetode();
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
                    String temp = String.valueOf(interclass.getName().charAt(0));
                    if(temp.equals("+"))
                        temp = "\\+";

                    String ime = interclass.getName().split(temp)[1];
                    stringBuilder.append(" implements " + ime);
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

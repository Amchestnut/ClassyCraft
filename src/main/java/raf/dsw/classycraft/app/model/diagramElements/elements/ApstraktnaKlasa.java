package raf.dsw.classycraft.app.model.diagramElements.elements;

import raf.dsw.classycraft.app.model.diagramElements.connections.Connection;
import raf.dsw.classycraft.app.model.diagramElements.connections.Nasledjivanje;
import raf.dsw.classycraft.app.model.diagramElements.connections.Realizacija;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ApstraktnaKlasa extends Interclass{
    private List<ClassContent> attributes;
    private List<ClassContent> methods;

    public ApstraktnaKlasa(int color, int stroke, String name, Point location, Dimension dimension) {       // recept za crtanje
        super(color, stroke, name, location, dimension);
        attributes = new ArrayList<>();
        methods = new ArrayList<>();
    }
    public ApstraktnaKlasa(){ //za Json, nema drugu svrhu
        attributes = new ArrayList<>();
        methods = new ArrayList<>();
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

        stringBuilder.append(vidljiv + "abstract class " + getName().split(tmp)[1] + doIExtendOrImplementSomething + "{\n");
        for(ClassContent classContent : attributes){
            stringBuilder.append(classContent.export() + "\n");
        }
        for(ClassContent classContent : methods){
            stringBuilder.append(classContent.export() + "\n");
        }
        if(doIExtendOrImplementSomething != null){
            stringBuilder.append(ispisiAtributeIMetodeNasledjeneKlase());
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

    public String ispisiAtributeIMetodeNasledjeneKlase(){
        Interclass staNasledjujem;
        for(Connection connection : getAllConnectionsOnThisInterclass()){
            if(connection.getInterclassOD().equals(this)){
                staNasledjujem = connection.getInterclassDO();
                return staNasledjujem.ispisiMetode();
            }
        }
        return "";
    }

    public String getAbstraction(){
        StringBuilder stringBuilder = new StringBuilder();

        for(Connection connection : getAllConnectionsOnThisInterclass()){
            if(connection.getInterclassOD().equals(this)){
                Interclass interclass = connection.getInterclassDO();
                if(connection instanceof Realizacija) {
                    stringBuilder.append(" implements ").append(interclass.getName());
                }
                else if(connection instanceof Nasledjivanje){
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

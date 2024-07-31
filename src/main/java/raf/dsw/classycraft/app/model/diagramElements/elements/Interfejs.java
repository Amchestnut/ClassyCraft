package raf.dsw.classycraft.app.model.diagramElements.elements;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Interfejs extends Interclass {
    private List<ClassContent> methods;
    public Interfejs(int color, int stroke, String name, Point location, Dimension dimension) {
        super(color, stroke, name, location, dimension);
        methods = new ArrayList<>();
    }
    public Interfejs(){ //za Json, nema drugu svrhu
        methods = new ArrayList<>();
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
    @Override
    public String export() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("public interface " + getName() + " {\n");
        for(ClassContent classContent : methods){
            stringBuilder.append(classContent.export() + "\n");
        }

        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public List<ClassContent> getMethods() {
        return methods;
    }
    public void setMethods(List<ClassContent> methods) {
        this.methods = methods;
    }
}

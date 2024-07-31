package raf.dsw.classycraft.app.model.diagramElements.elements;

public class Atribut extends ClassContent{
    private String attributeName;
    public Atribut(String vidljivost, String attributeName) {
        super(vidljivost);
        this.attributeName = attributeName;
    }

    @Override
    public String export() {
        StringBuilder stringBuilder = new StringBuilder();

        String[] split = attributeName.split(":");
        String povratnaVr = split[1];
        String ime = split[0];
        String vidljivost = getVidljivost();
        if(vidljivost.equals("+"))
            vidljivost = "public";
        else if(vidljivost.equals("-"))
            vidljivost = "private";
        else if(vidljivost.equals("#"))
            vidljivost = "protected";

        stringBuilder.append(vidljivost).append(povratnaVr).append(" ").append(ime).append(";");

        return stringBuilder.toString();
    }

    public Atribut(){       //Za JSON
        super();
    }
    public String getAttributeName() {
        return attributeName;
    }
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }
}

package raf.dsw.classycraft.app.model.diagramElements.elements;

public abstract class ClassContent {

    protected String vidljivost;
    public abstract String export();
    public ClassContent(String vidljivost) {
        this.vidljivost = vidljivost;
    }
    public ClassContent(){    // For jackson

    }
    public String getVidljivost() {
        return vidljivost;
    }

    public void setVidljivost(String vidljivost) {
        this.vidljivost = vidljivost;
    }
}

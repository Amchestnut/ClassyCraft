package raf.dsw.classycraft.app.model.diagramElements.connections;

import raf.dsw.classycraft.app.model.diagramElements.elements.Interclass;

public class DataForConnection {

    private String type;
    private String visibilityOfTheFirstElement = "";
    private String instanceOfTheFirstElement = "";
    private String cardinalityOfTheFirstElement = "";

    private String visibilityOfTheSecondElement = "";
    private String instanceOfTheSecondElement = "";
    private String cardinalityOfTheSecondElement = "";

    private Interclass interclassFROM;
    private Interclass interclassTO;

    public DataForConnection(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVisibilityOfTheFirstElement() {
        return visibilityOfTheFirstElement;
    }

    public void setVisibilityOfTheFirstElement(String visibilityOfTheFirstElement) {
        this.visibilityOfTheFirstElement = visibilityOfTheFirstElement;
    }

    public String getVisibilityOfTheSecondElement() {
        return visibilityOfTheSecondElement;
    }

    public void setVisibilityOfTheSecondElement(String visibilityOfTheSecondElement) {
        this.visibilityOfTheSecondElement = visibilityOfTheSecondElement;
    }

    public String getInstanceOfTheFirstElement() {
        return instanceOfTheFirstElement;
    }

    public void setInstanceOfTheFirstElement(String instanceOfTheFirstElement) {
        this.instanceOfTheFirstElement = instanceOfTheFirstElement;
    }

    public String getInstanceOfTheSecondElement() {
        return instanceOfTheSecondElement;
    }

    public void setInstanceOfTheSecondElement(String instanceOfTheSecondElement) {
        this.instanceOfTheSecondElement = instanceOfTheSecondElement;
    }

    public String getCardinalityOfTheFirstElement() {
        return cardinalityOfTheFirstElement;
    }

    public void setCardinalityOfTheFirstElement(String cardinalityOfTheFirstElement) {
        this.cardinalityOfTheFirstElement = cardinalityOfTheFirstElement;
    }

    public String getCardinalityOfTheSecondElement() {
        return cardinalityOfTheSecondElement;
    }

    public void setCardinalityOfTheSecondElement(String cardinalityOfTheSecondElement) {
        this.cardinalityOfTheSecondElement = cardinalityOfTheSecondElement;
    }

    public Interclass getInterclassFROM() {
        return interclassFROM;
    }

    public void setInterclassFROM(Interclass interclassFROM) {
        this.interclassFROM = interclassFROM;
    }

    public Interclass getInterclassTO() {
        return interclassTO;
    }

    public void setInterclassTO(Interclass interclassTO) {
        this.interclassTO = interclassTO;
    }
}

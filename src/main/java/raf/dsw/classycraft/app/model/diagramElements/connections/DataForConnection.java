package raf.dsw.classycraft.app.model.diagramElements.connections;

import raf.dsw.classycraft.app.model.diagramElements.elements.Interclass;

public class DataForConnection {

    private String type;
    private String visibilityOfTheFirstElement = "";
    private String instanceOfTheFirstElement = "";
    private String kardinalnostOfTheFirstElement = "";  // multiplicity

    private String visibilityOfTheSecondElement = "";
    private String instanceOfTheSecondElement = "";
    private String kardinalnostOfTheSecondElement = "";

    private Interclass interclassOD;
    private Interclass interclassDO;

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

    public String getKardinalnostOfTheFirstElement() {
        return kardinalnostOfTheFirstElement;
    }

    public void setKardinalnostOfTheFirstElement(String kardinalnostOfTheFirstElement) {
        this.kardinalnostOfTheFirstElement = kardinalnostOfTheFirstElement;
    }

    public String getKardinalnostOfTheSecondElement() {
        return kardinalnostOfTheSecondElement;
    }

    public void setKardinalnostOfTheSecondElement(String kardinalnostOfTheSecondElement) {
        this.kardinalnostOfTheSecondElement = kardinalnostOfTheSecondElement;
    }

    public Interclass getInterclassOD() {
        return interclassOD;
    }

    public void setInterclassOD(Interclass interclassOD) {
        this.interclassOD = interclassOD;
    }

    public Interclass getInterclassDO() {
        return interclassDO;
    }

    public void setInterclassDO(Interclass interclassDO) {
        this.interclassDO = interclassDO;
    }
}

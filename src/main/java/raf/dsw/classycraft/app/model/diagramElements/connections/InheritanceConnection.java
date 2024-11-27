package raf.dsw.classycraft.app.model.diagramElements.connections;

import com.fasterxml.jackson.annotation.JsonTypeName;
import raf.dsw.classycraft.app.model.diagramElements.elements.Interclass;

@JsonTypeName("InheritanceConnection")
public class InheritanceConnection extends Connection{
    public InheritanceConnection(int color, int stroke, String name, String type, Interclass interclassOD, Interclass interclassDO) {
        super(color, stroke, name, type, interclassOD, interclassDO);
    }
    public InheritanceConnection(){         //za Json, nema drugu svrhu

    }
}

package raf.dsw.classycraft.app.model.diagramElements.connections;

import com.fasterxml.jackson.annotation.JsonTypeName;
import raf.dsw.classycraft.app.model.diagramElements.elements.Interclass;

@JsonTypeName("AssociationConnection")
public class AssociationConnection extends Connection{
    public AssociationConnection(int color, int stroke, String name, String type, Interclass interclassOD, Interclass interclassDO) {
        super(color, stroke, name, type, interclassOD, interclassDO);
    }

    public AssociationConnection(){   //za Json, nema drugu svrhu

    }
}

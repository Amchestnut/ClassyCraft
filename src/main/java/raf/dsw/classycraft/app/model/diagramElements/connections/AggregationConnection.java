package raf.dsw.classycraft.app.model.diagramElements.connections;

import com.fasterxml.jackson.annotation.JsonTypeName;
import raf.dsw.classycraft.app.model.diagramElements.elements.Interclass;

@JsonTypeName("AggregationConnection")
public class AggregationConnection extends Connection{

    public AggregationConnection(int color, int stroke, String name, String type, Interclass interclassFROM, Interclass interclassTO) {
        super(color, stroke, name, type, interclassFROM, interclassTO);
    }

    public AggregationConnection(){    // For jackson

    }
}

package raf.dsw.classycraft.app.model.diagramElements.connections;

import raf.dsw.classycraft.app.model.diagramElements.elements.Interclass;

public class Realizacija extends Connection{
    public Realizacija(int color, int stroke, String name, String type, Interclass interclassOD, Interclass interclassDO) {
        super(color, stroke, name, type, interclassOD, interclassDO);
    }

    public Realizacija(){       //za Json, nema drugu svrhu

    }
}

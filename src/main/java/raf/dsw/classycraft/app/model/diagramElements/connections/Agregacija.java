package raf.dsw.classycraft.app.model.diagramElements.connections;

import raf.dsw.classycraft.app.model.diagramElements.elements.Interclass;

public class Agregacija extends Connection{

    public Agregacija(int color, int stroke, String name, String type, Interclass interclassOD, Interclass interclassDO) {
        super(color, stroke, name, type, interclassOD, interclassDO);
    }

    public Agregacija(){    //za Json, nema drugu svrhu

    }
}

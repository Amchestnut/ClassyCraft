package raf.dsw.classycraft.app.model.diagramElements.connections;

import raf.dsw.classycraft.app.model.diagramElements.elements.Interclass;

public class Zavisnost extends Connection{
    public Zavisnost(int color, int stroke, String name, String type, Interclass interclassOD, Interclass interclassDO) {
        super(color, stroke, name, type, interclassOD, interclassDO);
    }
    public Zavisnost(){     //za Json, nema drugu svrhu

    }
}

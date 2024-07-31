package raf.dsw.classycraft.app.model.diagramElements.connections;

import raf.dsw.classycraft.app.model.diagramElements.elements.Interclass;

public class Nasledjivanje extends Connection{
    public Nasledjivanje(int color, int stroke, String name, String type, Interclass interclassOD, Interclass interclassDO) {
        super(color, stroke, name, type, interclassOD, interclassDO);
    }
    public Nasledjivanje(){         //za Json, nema drugu svrhu

    }
}

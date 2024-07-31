package raf.dsw.classycraft.app.model.state.classesAndConnections;

import raf.dsw.classycraft.app.model.diagramElements.DiagramElement;
import raf.dsw.classycraft.app.model.diagramElements.connections.DataForConnection;
import raf.dsw.classycraft.app.model.diagramElements.elements.DataForElementFromDialog;

import java.awt.*;

public abstract class AbstractFactory {

    public abstract DiagramElement createInterclass(Point location, DataForElementFromDialog dataForElementFromDialog);
    public abstract DiagramElement createConnection(DataForConnection dataForConnection);

}

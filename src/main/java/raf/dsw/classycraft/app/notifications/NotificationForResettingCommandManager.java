package raf.dsw.classycraft.app.notifications;

import raf.dsw.classycraft.app.model.modelImplementation.Diagram;

public class NotificationForResettingCommandManager {
    Diagram diagram;

    public NotificationForResettingCommandManager(Diagram diagram) {
        this.diagram = diagram;
    }

    public Diagram getDiagram() {
        return diagram;
    }

    public void setDiagram(Diagram diagram) {
        this.diagram = diagram;
    }
}

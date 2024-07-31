package raf.dsw.classycraft.app.model.state;

import raf.dsw.classycraft.app.model.state.classesAndConnections.AddDuplicateState;
import raf.dsw.classycraft.app.model.state.classesAndConnections.AddInterclassState;
import raf.dsw.classycraft.app.model.state.connections.AddConnectionState;
import raf.dsw.classycraft.app.model.state.functions.*;

public class StateManager {
    // stejtovi za dodavanje
    private State currentState;
    private AddInterclassState addInterclassState;

    // funkcije
    private SelectState selectState;
    private MoveState moveState;
    private DeleteState deleteState;
    private EditState editState;
    private ZoomState zoomState;
    private AddDuplicateState addDuplicateState;
    private ZoomToFitState zoomToFitState;

    // veze
    private AddConnectionState addConnectionState;

    public StateManager() {
        initialize();
    }

    private void initialize() {
        addInterclassState = new AddInterclassState();
        selectState = new SelectState();
        moveState = new MoveState();
        deleteState = new DeleteState();
        editState = new EditState();
        zoomState = new ZoomState();
        addDuplicateState = new AddDuplicateState();
        addConnectionState = new AddConnectionState();
        zoomToFitState = new ZoomToFitState();

        currentState = selectState;
    }

    public State getCurrentState() {            // zvacu svaki put ovu metodu da vidim koji je trenutni state
        return currentState;
    }

    public void setAddInterclassState(){
        currentState = addInterclassState;
    }

    public void setSelectState(){
        currentState = selectState;
    }

    public void setMoveState() {
        currentState = moveState;
    }

    public void setDeleteState() {
        currentState = deleteState;
    }

    public void setEditState(){
        currentState = editState;
    }

    public void setZoomToFitState() {
        currentState = zoomToFitState;
    }

    public void setZoomState() {
        currentState = zoomState;
    }

    public void setAddDuplicateState() {
        currentState = addDuplicateState;
    }

    public void setConnectionState() {
        currentState = addConnectionState;
    }
}

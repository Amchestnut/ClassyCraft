package raf.dsw.classycraft.app.controller;

import raf.dsw.classycraft.app.controller.stateActions.*;
import raf.dsw.classycraft.app.controller.upperToolbarActions.*;

public class ActionManager {
    private CodeGenerator codeGenerator;
    private ExitAction exitAction;
    private AboutUsAction aboutUsAction;
    private DeleteClassyNodeAction deleteClassyNodeAction;
    private AddClassyNodeAction addClassyNodeAction;
    private ChangeAuthorAction changeAuthorAction;
    private GuideAction guideAction;
    private UndoAction undoAction;
    private RedoAction redoAction;
    private LoadProjectAction loadProjectAction;
    private SaveProjectAction saveProjectAction;
    private LoadDiagramAction loadDiagramAction;
    private SaveDiagramAction saveDiagramAction;
    private ScreenshotAction screenshotAction;
    private ExportDiagramToPNGAction exportDiagramToPNGAction;

    private AddInterclassAction addInterclassAction;
    private AddConnectionAction addConnectionAction;
    private AddDuplicateAction addDuplicateAction;
    private DeleteElementAction deleteElementAction;
    private MoveAction moveAction;
    private SelectionAction selectionAction;
    private ZoomAction zoomAction;
    private EditElementAction editElementAction;
    private ZoomToFitAction zoomToFitAction;

    public ActionManager(){
        initialiseActions();
    }
    private void initialiseActions(){
        exitAction = new ExitAction();
        aboutUsAction = new AboutUsAction();
        deleteClassyNodeAction = new DeleteClassyNodeAction();
        addClassyNodeAction = new AddClassyNodeAction();
        changeAuthorAction = new ChangeAuthorAction();
        screenshotAction = new ScreenshotAction();
        exportDiagramToPNGAction = new ExportDiagramToPNGAction();
        codeGenerator = new CodeGenerator();

        undoAction = new UndoAction();
        redoAction = new RedoAction();
        guideAction = new GuideAction();

        loadDiagramAction = new LoadDiagramAction();
        saveDiagramAction = new SaveDiagramAction();
        loadProjectAction = new LoadProjectAction();
        saveProjectAction = new SaveProjectAction();

        addInterclassAction = new AddInterclassAction();
        addConnectionAction = new AddConnectionAction();
        addDuplicateAction = new AddDuplicateAction();
        deleteElementAction = new DeleteElementAction();
        moveAction = new MoveAction();
        selectionAction = new SelectionAction();
        zoomAction = new ZoomAction();
        editElementAction = new EditElementAction();
        zoomToFitAction = new ZoomToFitAction();
    }

    public CodeGenerator getCodeGenerator() {
        return codeGenerator;
    }

    public void setCodeGenerator(CodeGenerator codeGenerator) {
        this.codeGenerator = codeGenerator;
    }

    public LoadProjectAction getLoadProjectAction() {
        return loadProjectAction;
    }

    public void setLoadProjectAction(LoadProjectAction loadProjectAction) {
        this.loadProjectAction = loadProjectAction;
    }

    public SaveProjectAction getSaveProjectAction() {
        return saveProjectAction;
    }

    public void setSaveProjectAction(SaveProjectAction saveProjectAction) {
        this.saveProjectAction = saveProjectAction;
    }

    public LoadDiagramAction getLoadDiagramAction() {
        return loadDiagramAction;
    }

    public void setLoadDiagramAction(LoadDiagramAction loadDiagramAction) {
        this.loadDiagramAction = loadDiagramAction;
    }

    public SaveDiagramAction getSaveDiagramAction() {
        return saveDiagramAction;
    }

    public void setSaveDiagramAction(SaveDiagramAction saveDiagramAction) {
        this.saveDiagramAction = saveDiagramAction;
    }

    public UndoAction getUndoAction() {
        return undoAction;
    }

    public void setUndoAction(UndoAction undoAction) {
        this.undoAction = undoAction;
    }

    public RedoAction getRedoAction() {
        return redoAction;
    }

    public void setRedoAction(RedoAction redoAction) {
        this.redoAction = redoAction;
    }

    public EditElementAction getEditElementAction() {
        return editElementAction;
    }

    public void setEditElementAction(EditElementAction editElementAction) {
        this.editElementAction = editElementAction;
    }

    public AddConnectionAction getAddConnectionAction() {
        return addConnectionAction;
    }

    public void setAddConnectionAction(AddConnectionAction addConnectionAction) {
        this.addConnectionAction = addConnectionAction;
    }

    public AddDuplicateAction getAddDuplicateAction() {
        return addDuplicateAction;
    }

    public void setAddDuplicateAction(AddDuplicateAction addDuplicateAction) {
        this.addDuplicateAction = addDuplicateAction;
    }

    public DeleteElementAction getDeleteElementAction() {
        return deleteElementAction;
    }

    public void setDeleteElementAction(DeleteElementAction deleteElementAction) {
        this.deleteElementAction = deleteElementAction;
    }

    public MoveAction getMoveAction() {
        return moveAction;
    }

    public void setMoveAction(MoveAction moveAction) {
        this.moveAction = moveAction;
    }

    public SelectionAction getSelectionAction() {
        return selectionAction;
    }

    public void setSelectionAction(SelectionAction selectionAction) {
        this.selectionAction = selectionAction;
    }

    public ZoomAction getZoomAction() {
        return zoomAction;
    }

    public void setZoomAction(ZoomAction zoomAction) {
        this.zoomAction = zoomAction;
    }

    public AddInterclassAction getAddInterclassAction() {
        return addInterclassAction;
    }

    public void setAddInterclassAction(AddInterclassAction addInterclassAction) {
        this.addInterclassAction = addInterclassAction;
    }

    public ChangeAuthorAction getChangeAuthorAction() {
        return changeAuthorAction;
    }

    public void setChangeAuthorAction(ChangeAuthorAction changeAuthorAction) {
        this.changeAuthorAction = changeAuthorAction;
    }

    public ExitAction getExitAction() {
        return exitAction;
    }

    public void setExitAction(ExitAction exitAction) {
        this.exitAction = exitAction;
    }

    public AboutUsAction getAboutUsAction() {
        return aboutUsAction;
    }

    public void setAboutUsAction(AboutUsAction aboutUsAction) {
        this.aboutUsAction = aboutUsAction;
    }

    public DeleteClassyNodeAction getDeleteClassyNodeAction() {
        return deleteClassyNodeAction;
    }

    public void setDeleteClassyNodeAction(DeleteClassyNodeAction deleteClassyNodeAction) {
        this.deleteClassyNodeAction = deleteClassyNodeAction;
    }

    public AddClassyNodeAction getAddClassyNodeAction() {
        return addClassyNodeAction;
    }

    public void setAddClassyNodeAction(AddClassyNodeAction addClassyNodeAction) {
        this.addClassyNodeAction = addClassyNodeAction;
    }

    public ZoomToFitAction getZoomToFitAction() {
        return zoomToFitAction;
    }

    public void setZoomToFitAction(ZoomToFitAction zoomToFitAction) {
        this.zoomToFitAction = zoomToFitAction;
    }

    public GuideAction getGuideAction() {
        return guideAction;
    }

    public void setGuideAction(GuideAction guideAction) {
        this.guideAction = guideAction;
    }

    public ScreenshotAction getScreenshotAction() {
        return screenshotAction;
    }

    public void setScreenshotAction(ScreenshotAction screenshotAction) {
        this.screenshotAction = screenshotAction;
    }

    public ExportDiagramToPNGAction getExportDiagramToPNGAction() {
        return exportDiagramToPNGAction;
    }

    public void setExportDiagramToPNGAction(ExportDiagramToPNGAction exportDiagramToPNGAction) {
        this.exportDiagramToPNGAction = exportDiagramToPNGAction;
    }
}

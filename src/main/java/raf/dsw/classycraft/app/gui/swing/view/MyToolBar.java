package raf.dsw.classycraft.app.gui.swing.view;

import javax.swing.*;

public class MyToolBar extends JToolBar {
    public MyToolBar(){
        super(HORIZONTAL);
        setFloatable(false);

        add(MainFrame.getInstance().getActionManager().getExitAction());
        add(MainFrame.getInstance().getActionManager().getAboutUsAction());
        add(MainFrame.getInstance().getActionManager().getAddClassyNodeAction());
        add(MainFrame.getInstance().getActionManager().getDeleteClassyNodeAction());
        add(MainFrame.getInstance().getActionManager().getChangeAuthorAction());
        add(MainFrame.getInstance().getActionManager().getUndoAction());
        add(MainFrame.getInstance().getActionManager().getRedoAction());
        add(MainFrame.getInstance().getActionManager().getLoadProjectAction());
        add(MainFrame.getInstance().getActionManager().getSaveProjectAction());
        add(MainFrame.getInstance().getActionManager().getLoadDiagramAction());
        add(MainFrame.getInstance().getActionManager().getSaveDiagramAction());
        add(MainFrame.getInstance().getActionManager().getScreenshotAction());
        add(MainFrame.getInstance().getActionManager().getExportDiagramToPNGAction());
        add(MainFrame.getInstance().getActionManager().getCodeGenerator());
    }
}
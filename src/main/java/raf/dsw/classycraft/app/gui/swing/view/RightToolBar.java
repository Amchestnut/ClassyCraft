package raf.dsw.classycraft.app.gui.swing.view;

import javax.swing.*;

public class RightToolBar extends JToolBar {
    public RightToolBar() {
        super(VERTICAL);
        setFloatable(false);

        add(MainFrame.getInstance().getActionManager().getGuideAction());
        add(MainFrame.getInstance().getActionManager().getSelectionAction());
        add(MainFrame.getInstance().getActionManager().getMoveAction());
        add(MainFrame.getInstance().getActionManager().getAddInterclassAction());
        add(MainFrame.getInstance().getActionManager().getAddConnectionAction());
        add(MainFrame.getInstance().getActionManager().getEditElementAction());
        add(MainFrame.getInstance().getActionManager().getDeleteElementAction());
        add(MainFrame.getInstance().getActionManager().getAddDuplicateAction());
        add(MainFrame.getInstance().getActionManager().getZoomAction());
        add(MainFrame.getInstance().getActionManager().getZoomToFitAction());
    }
}

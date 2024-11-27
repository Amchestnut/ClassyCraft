package raf.dsw.classycraft.app.gui.swing.tree.view;

import raf.dsw.classycraft.app.gui.swing.tree.controller.ClassyTreeCellEditor;
import raf.dsw.classycraft.app.gui.swing.tree.controller.ClassyTreeSelectionListener;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;

/*
This is essentially our JTree implementation.

Before creating it, we need to provide:
    1) All its TreeItems
    2) A model
    3) A tree selection listener
    4) A tree cell editor
    5) A tree cell renderer
 */


// ClassyTreeView extends JTree and adds custom behavior for rendering, editing, and selection.
public class ClassyTreeView extends JTree {
    public ClassyTreeView(DefaultTreeModel defaultTreeModel) {
        setModel(defaultTreeModel);
        ClassyTreeCellRenderer classyTreeCellRenderer = new ClassyTreeCellRenderer();

        addTreeSelectionListener(new ClassyTreeSelectionListener());
        setCellEditor(new ClassyTreeCellEditor(this, classyTreeCellRenderer));
        setCellRenderer(classyTreeCellRenderer);
        setEditable(true);    // Allows cell editing (example: editing with a triple-click using the mouse)
    }
}


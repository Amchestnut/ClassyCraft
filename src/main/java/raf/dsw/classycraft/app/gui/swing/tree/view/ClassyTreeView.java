package raf.dsw.classycraft.app.gui.swing.tree.view;

import raf.dsw.classycraft.app.gui.swing.tree.controller.ClassyTreeCellEditor;
import raf.dsw.classycraft.app.gui.swing.tree.controller.ClassyTreeSelectionListener;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;

/*
Ovo je zapravo nas: JTree.

Pre nego sto ga napravim, moram da mu dam:
    1) sve njegove TreeItem-e
    2) model
    3) tree selection listener
    4) tree cell editor
    5) tree cell rendener

 */
public class ClassyTreeView extends JTree {
    public ClassyTreeView(DefaultTreeModel defaultTreeModel) {
        setModel(defaultTreeModel);
        ClassyTreeCellRenderer classyTreeCellRenderer = new ClassyTreeCellRenderer();

        addTreeSelectionListener(new ClassyTreeSelectionListener());
        setCellEditor(new ClassyTreeCellEditor(this, classyTreeCellRenderer));
        setCellRenderer(classyTreeCellRenderer);
        setEditable(true);                          // dozvoljava da moze da se desi cellEditor, ono sa tri klika misom --- za edit
    }
}


package raf.dsw.classycraft.app.gui.swing.tree;

import raf.dsw.classycraft.app.gui.swing.tree.model.ClassyTreeItem;
import raf.dsw.classycraft.app.gui.swing.tree.view.ClassyTreeView;
import raf.dsw.classycraft.app.model.diagramElements.DiagramElement;
import raf.dsw.classycraft.app.model.modelImplementation.ProjectExplorer;

public interface ClassyTree {

    ClassyTreeView generateTree(ProjectExplorer projectExplorer);
    ClassyTreeItem getRoot();
    ClassyTreeView getTreeView();
    ClassyTreeItem getSelectedNode();
    void addChild(ClassyTreeItem parent);
    void deleteNode(ClassyTreeItem parent);
    void addChildToDiagram(ClassyTreeItem classyTreeItemReference, DiagramElement diagramElement);
}


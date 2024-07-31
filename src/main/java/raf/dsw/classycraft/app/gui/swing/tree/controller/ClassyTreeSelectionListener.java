package raf.dsw.classycraft.app.gui.swing.tree.controller;

import raf.dsw.classycraft.app.gui.swing.tree.model.ClassyTreeItem;
import raf.dsw.classycraft.app.gui.swing.view.MainFrame;
import raf.dsw.classycraft.app.model.modelImplementation.Package;
import raf.dsw.classycraft.app.model.modelImplementation.Project;
import raf.dsw.classycraft.app.model.repositoryModel.ClassyNode;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ClassyTreeSelectionListener implements TreeSelectionListener {

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        TreePath path = e.getPath();                                                     // putanja do cvora koji smo kliknuli
        ClassyTreeItem treeItemSelected = (ClassyTreeItem)path.getLastPathComponent();   // poslednji iz putanje (kliknuti cvor)
        System.out.println("Selektovan cvor : "+ treeItemSelected.getClassyNode().getName());
        System.out.println("getPath: " + e.getPath());

        if(treeItemSelected.getClassyNode() instanceof Project){
            boolean changed = ((Project) treeItemSelected.getClassyNode()).isChanged();
            MainFrame.getInstance().getActionManager().getSaveProjectAction().setEnabled(changed);
        }

        if(treeItemSelected.getClassyNode() instanceof Package){
            MainFrame.getInstance().getProjectExplorer().addMouseListener(new MouseAdapter()  {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) { // Check for double-click
                        TreePath path = MainFrame.getInstance().getProjectExplorer().getPathForLocation(e.getX(), e.getY());
                        if (path != null) {
                            DefaultMutableTreeNode selected = (DefaultMutableTreeNode) path.getLastPathComponent();     //Selektovan node u drvetu
                            if(selected instanceof ClassyTreeItem){
                                ClassyNode nodeOfSelected = ((ClassyTreeItem) selected).getClassyNode();                //Selektovan ClassyNode u drvetu

                                if(nodeOfSelected instanceof Package){
                                    ((Package) nodeOfSelected).openPackage();                                           //Otvaranje paketa
                                }
                            }

                        }
                    }
                }
            });
        }
    }
}

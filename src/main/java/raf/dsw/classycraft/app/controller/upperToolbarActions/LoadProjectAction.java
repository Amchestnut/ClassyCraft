package raf.dsw.classycraft.app.controller.upperToolbarActions;

import raf.dsw.classycraft.app.controller.AbstractClassyAction;
import raf.dsw.classycraft.app.core.ApplicationFramework;
import raf.dsw.classycraft.app.gui.swing.tree.model.ClassyTreeItem;
import raf.dsw.classycraft.app.gui.swing.view.MainFrame;
import raf.dsw.classycraft.app.model.diagramElements.DiagramElement;
import raf.dsw.classycraft.app.model.modelImplementation.Diagram;
import raf.dsw.classycraft.app.model.modelImplementation.Package;
import raf.dsw.classycraft.app.model.modelImplementation.Project;
import raf.dsw.classycraft.app.model.modelImplementation.ProjectExplorer;
import raf.dsw.classycraft.app.model.repositoryModel.ClassyNode;
import raf.dsw.classycraft.app.model.repositoryModel.ClassyNodeComposite;
import raf.dsw.classycraft.app.notifications.NotificationForOpeningPackage;
import raf.dsw.classycraft.app.notifications.NotificationForResettingCommandManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;

public class LoadProjectAction extends AbstractClassyAction {
    public LoadProjectAction(){
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
        putValue(SMALL_ICON, scaleIcon(loadIcon("/images/load_project.png"), 32, 32));
        putValue(NAME, "Load project");
        putValue(SHORT_DESCRIPTION, "Load project");
    }
    @Override
    public void actionPerformed(ActionEvent arg0) {
        JFileChooser jFileChooser = new JFileChooser();

        if (jFileChooser.showOpenDialog(MainFrame.getInstance()) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = jFileChooser.getSelectedFile();
                Project project = ApplicationFramework.getInstance().getSerializer().loadProject(file);
                MainFrame.getInstance().getPackageView().repaint();
                loadProject(project);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void loadProject(Project project) {
        if (project == null) {
            return;   // Project is null, skipping load
        }

        // Set loading mode
        MainFrame.getInstance().getCommandManager().setLoading(true);

        try {
            ClassyTreeItem parent = MainFrame.getInstance().getClassyTree().getRoot();
            MainFrame.getInstance().getPackageView().dodajMeKaoSubscribera(project);
            ClassyTreeItem itemForProject = new ClassyTreeItem(project);
            ((ClassyNodeComposite) parent.getClassyNode()).addChild(project);
            parent.add(itemForProject);
            addChildrenFromLoadProjectRecurively(null, itemForProject);

            if (parent.getClassyNode() instanceof ProjectExplorer || parent.getClassyNode() instanceof Project) {
                MainFrame.getInstance().getClassyTree().getTreeView().expandPath(
                        MainFrame.getInstance().getClassyTree().getTreeView().getSelectionPath()
                );
            }
            SwingUtilities.updateComponentTreeUI(MainFrame.getInstance().getClassyTree().getTreeView());
            MainFrame.getInstance().getPackageView().repaint();
        }
        finally {
            // Reset loading mode
            MainFrame.getInstance().getCommandManager().setLoading(false);
        }
    }

    private ClassyTreeItem addChildrenFromLoadProjectRecurively(ClassyTreeItem parent, ClassyTreeItem child){
        if(child == null)
            return null;
        MainFrame.getInstance().getPackageView().dodajMeKaoSubscribera(child.getClassyNode());
        if(child.getClassyNode() instanceof ClassyNodeComposite){
            if(parent != null) {
                parent.add(child);
            }
            for(ClassyNode childOfChild : ((ClassyNodeComposite) child.getClassyNode()).getChildren()){         // Goes through the list of children and adds them to the tree
                childOfChild.setParent(child.getClassyNode());

                ClassyTreeItem novi = addChildrenFromLoadProjectRecurively(child, new ClassyTreeItem(childOfChild));
                if(novi != null) {
                    MainFrame.getInstance().getPackageView().dodajMeKaoSubscribera(novi.getClassyNode());
                    novi.getClassyNode().setParent(child.getClassyNode());
                }
            }
        }
        if(child.getClassyNode() instanceof Package && parent.getClassyNode() instanceof Project) {
            if(child.getClassyNode().getParent() == null){
                child.getClassyNode().setParent(parent.getClassyNode());
            }
            // If the child is a package, open it (so the diagramView's can be made)
            ((Package) child.getClassyNode()).notifySubscribers(new NotificationForOpeningPackage(child.getClassyNode(), null));
        }
        if(child.getClassyNode() instanceof Diagram) {
            addChildToDiagramFromLoadProject(child, parent);    // Recursion for classes and connections
            ((Diagram) child.getClassyNode()).notifySubscribers(new NotificationForResettingCommandManager((Diagram) child.getClassyNode()));
        }
        return child;
    }

    // Recursion for classes and connections
    public void addChildToDiagramFromLoadProject(ClassyTreeItem diagramItem, ClassyTreeItem parent) {
        if (diagramItem.getClassyNode() instanceof Diagram) {

            if(parent.getClassyNode() instanceof Package){
                ((Package) parent.getClassyNode()).notifySubscribers(new NotificationForOpeningPackage(parent.getClassyNode(), diagramItem.getClassyNode()));
                System.out.println("AAA");
            }

            Diagram diagram = (Diagram) diagramItem.getClassyNode();
            if (parent.getClassyNode() instanceof Package && diagram.getParent() == null) {
                diagram.setParent(parent.getClassyNode());
            }
            for(ClassyNode element : diagram.getChildren()){
                if(!(element instanceof DiagramElement))
                    continue;
                element.setParent(diagram);
                SwingUtilities.updateComponentTreeUI(MainFrame.getInstance().getClassyTree().getTreeView());    // Show yourself now
            }
        }
    }
}

package raf.dsw.classycraft.app.gui.swing.tree;

import raf.dsw.classycraft.app.core.ApplicationFramework;
import raf.dsw.classycraft.app.gui.swing.messanger.MessageType;
import raf.dsw.classycraft.app.gui.swing.tree.model.ClassyTreeItem;
import raf.dsw.classycraft.app.gui.swing.tree.view.ClassyTreeView;
import raf.dsw.classycraft.app.gui.swing.view.MainFrame;
import raf.dsw.classycraft.app.model.diagramElements.DiagramElement;
import raf.dsw.classycraft.app.model.factories.DiagramFactory;
import raf.dsw.classycraft.app.model.factories.FactoryMethod;
import raf.dsw.classycraft.app.model.factories.PackageFactory;
import raf.dsw.classycraft.app.model.factories.Utils;
import raf.dsw.classycraft.app.model.modelImplementation.Diagram;
import raf.dsw.classycraft.app.model.modelImplementation.Package;
import raf.dsw.classycraft.app.model.modelImplementation.Project;
import raf.dsw.classycraft.app.model.modelImplementation.ProjectExplorer;
import raf.dsw.classycraft.app.model.repositoryModel.ClassyNode;
import raf.dsw.classycraft.app.model.repositoryModel.ClassyNodeComposite;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;

public class ClassyTreeImplementation implements ClassyTree {

    private ClassyTreeView treeView;
    private DefaultTreeModel treeModel;
    private ClassyTreeItem root;

    @Override
    public ClassyTreeView generateTree(ProjectExplorer projectExplorer) {
        root = new ClassyTreeItem(projectExplorer);
        treeModel = new DefaultTreeModel(root);
        treeView = new ClassyTreeView(treeModel);
        return treeView;
    }
    @Override
    public void addChild(ClassyTreeItem parent) {
        ClassyNode child = createChild(parent.getClassyNode());
        if(child == null)
            return;
        setChangedOnProject(child);
        dodajSubscribere(child);
        parent.add(new ClassyTreeItem(child)); // this is in order to show on the Tree (view) for JTree
        ((ClassyNodeComposite) parent.getClassyNode()).addChild(child);  // we need to add it also in the model (for the Model)

        if (parent.getClassyNode() instanceof ProjectExplorer || parent.getClassyNode() instanceof Project || parent.getClassyNode() instanceof Diagram)
            treeView.expandPath(treeView.getSelectionPath());
        SwingUtilities.updateComponentTreeUI(treeView); // "refresh"
    }
    public void addChildToDiagram(ClassyTreeItem parent, DiagramElement diagramElement) {
        if (parent.getClassyNode() instanceof Diagram) {
            // dodajemo i interclase i veze
            ClassyTreeItem child = new ClassyTreeItem(diagramElement);
            parent.add(child);
            treeView.expandPath(treeView.getSelectionPath()); // expand
            SwingUtilities.updateComponentTreeUI(treeView);   // show yourself immediately
        }
    }
    @Override
    public void deleteNode(ClassyTreeItem currentItem) {
        ClassyNode node = currentItem.getClassyNode();
        if (node instanceof ProjectExplorer) {
            ApplicationFramework.getInstance().getMessageGenerator().generateMessage("You can't delete Project Explorer", MessageType.ERROR);
            return;
        }
        currentItem.removeFromParent(); // From classyTreeItem, at this moment from the Left side we removed the Package for example

        // This is for deleting from the Right side
        if(node instanceof Diagram){
            ((Diagram) node).removeFromParent();
        }
        else if(node instanceof Package){
            ((Package) node).removeFromParent();
        }
        else if(node instanceof Project){
            ((Project) node).removeFromParent();
        }
        setChangedOnProject(node);
        SwingUtilities.updateComponentTreeUI(treeView);
    }
    private void dodajSubscribere(ClassyNode node){
        if(node instanceof Diagram || node instanceof Package || node instanceof Project){
            MainFrame.getInstance().getPackageView().dodajMeKaoSubscribera(node);
        }
    }

    private ClassyNode createChild(ClassyNode parent) {
        FactoryMethod factory = Utils.getFactoryForParentNode(parent);
        if (factory == null && parent instanceof Package) {
            factory = getUserChoiceForPackageChild();
        }
        if (factory != null) {
            return factory.createNode(parent);
        }
        return null;
    }
    private FactoryMethod getUserChoiceForPackageChild() {  // we do this here bcz Utils is not a UI component
        Object[] options = {"New Package", "New Diagram"};
        int choice = JOptionPane.showOptionDialog(null, // Parent component
                "What would you like to add to the package?", // Message
                "Select Node Type", // Title
                JOptionPane.YES_NO_OPTION, // Option type
                JOptionPane.QUESTION_MESSAGE, // Message type
                null, // icon
                options,
                options[0]); // Default option

        if (choice == JOptionPane.YES_OPTION)
            return new PackageFactory();
        else if (choice == JOptionPane.NO_OPTION)
            return new DiagramFactory();
        return null;
    }
    private void setChangedOnProject(ClassyNode node){
        while (!(node instanceof Project)){
            node = node.getParent();
        }
        ((Project) node).setChanged(true);
        MainFrame.getInstance().getActionManager().getSaveProjectAction().setEnabled(true);
    }
    @Override
    public ClassyTreeItem getSelectedNode() {
        return (ClassyTreeItem) treeView.getLastSelectedPathComponent();
    }
    @Override
    public ClassyTreeView getTreeView() {
        return treeView;
    }

    public void setTreeView(ClassyTreeView treeView) {
        this.treeView = treeView;
    }

    public DefaultTreeModel getTreeModel() {
        return treeModel;
    }

    public void setTreeModel(DefaultTreeModel treeModel) {
        this.treeModel = treeModel;
    }

    @Override
    public ClassyTreeItem getRoot() {
        return root;
    }

    public void setRoot(ClassyTreeItem root) {
        this.root = root;
    }
}


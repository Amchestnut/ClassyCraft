package raf.dsw.classycraft.app.controller.upperToolbarActions;

import raf.dsw.classycraft.app.controller.AbstractClassyAction;
import raf.dsw.classycraft.app.core.ApplicationFramework;
import raf.dsw.classycraft.app.gui.swing.messanger.MessageType;
import raf.dsw.classycraft.app.gui.swing.tree.model.ClassyTreeItem;
import raf.dsw.classycraft.app.gui.swing.view.MainFrame;
import raf.dsw.classycraft.app.model.diagramElements.DiagramElement;
import raf.dsw.classycraft.app.model.modelImplementation.Diagram;
import raf.dsw.classycraft.app.model.modelImplementation.Package;
import raf.dsw.classycraft.app.model.repositoryModel.ClassyNode;
import raf.dsw.classycraft.app.notifications.NotificationForOpeningPackage;
import raf.dsw.classycraft.app.notifications.NotificationForResettingCommandManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;

public class LoadDiagramAction extends AbstractClassyAction {
    public LoadDiagramAction() {
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
        putValue(SMALL_ICON, scaleIcon(loadIcon("/images/load.png"), 32, 32));
        putValue(NAME, "Load diagram");
        putValue(SHORT_DESCRIPTION, "Load diagram");
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        JFileChooser jFileChooser = new JFileChooser("src/main/resources/galleryOfDiagrams/");

        if (jFileChooser.showOpenDialog(MainFrame.getInstance()) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = jFileChooser.getSelectedFile();
                Diagram diagram = ApplicationFramework.getInstance().getSerializer().loadDiagram(file);
                loadDiagram(diagram);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void loadDiagram(Diagram diagram){
        ClassyTreeItem parentItem = MainFrame.getInstance().getClassyTree().getSelectedNode();
        if(!(parentItem.getClassyNode() instanceof Package)) {
            ApplicationFramework.getInstance().getMessageGenerator().generateMessage(
                    "Morate selektovati neki Package da bi ste ucitali diagram!", MessageType.ERROR);
            return;
        }
        MainFrame.getInstance().getPackageView().dodajMeKaoSubscribera(diagram);                                      //dodavanje subova

        Package parent = (Package)parentItem.getClassyNode();
        ClassyTreeItem diagramItem = new ClassyTreeItem(diagram);
        diagram.setParent(parent);                                      //u modelu dodajem caleta

        parent.addChild(diagram);                                       //dodaje decu u model
        parentItem.add(diagramItem);                                    //dodaje dete u drvo
        diagramItem.setParent(parentItem);

        parent.notifySubscribers(new NotificationForOpeningPackage(parent, diagram));

        addChildToDiagramFromLoadDiagram(diagramItem, parentItem);      //dodavanje dece

        diagram.notifySubscribers(new NotificationForResettingCommandManager(diagram));
        SwingUtilities.updateComponentTreeUI(MainFrame.getInstance().getClassyTree().getTreeView());
        MainFrame.getInstance().getPackageView().repaint();
    }
    public void addChildToDiagramFromLoadDiagram(ClassyTreeItem diagramItem, ClassyTreeItem parent) {                          //rekurzija za veze i klase (za sad samo klase jbg)
        if (diagramItem.getClassyNode() instanceof Diagram) {
            Diagram diagram = (Diagram) diagramItem.getClassyNode();

            if (parent.getClassyNode() instanceof Package && diagram.getParent() == null)
                diagram.setParent(parent.getClassyNode());

            for(ClassyNode element : diagram.getChildren()){
                if(!(element instanceof DiagramElement))
                    continue;

                element.setParent(diagram);
                SwingUtilities.updateComponentTreeUI(MainFrame.getInstance().getClassyTree().getTreeView());             // odmah se prikazi
            }
        }
    }
}

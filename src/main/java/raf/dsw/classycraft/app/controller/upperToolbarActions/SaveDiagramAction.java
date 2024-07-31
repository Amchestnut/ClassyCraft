package raf.dsw.classycraft.app.controller.upperToolbarActions;

import raf.dsw.classycraft.app.controller.AbstractClassyAction;
import raf.dsw.classycraft.app.core.ApplicationFramework;
import raf.dsw.classycraft.app.gui.swing.messanger.MessageType;
import raf.dsw.classycraft.app.gui.swing.view.MainFrame;
import raf.dsw.classycraft.app.model.modelImplementation.Diagram;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;

public class SaveDiagramAction extends AbstractClassyAction {
    public SaveDiagramAction(){
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
        putValue(SMALL_ICON, scaleIcon(loadIcon("/images/save_diagram.png"), 32, 32));
        putValue(NAME, "Save diagram");
        putValue(SHORT_DESCRIPTION, "Save diagram");
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(MainFrame.getInstance().getClassyTree().getSelectedNode() == null ||
                !(MainFrame.getInstance().getClassyTree().getSelectedNode().getClassyNode() instanceof Diagram)){
            ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Morate selektovati neki Diagram!", MessageType.ERROR);
            return;
        }

        Diagram diagram = (Diagram) MainFrame.getInstance().getClassyTree().getSelectedNode().getClassyNode();
        File diagramFile = new File("src/main/resources/galleryOfDiagrams/" + diagram.getName());

        if (diagram.getPath() == null || diagram.getPath().isEmpty()) {
            diagram.setPath(diagramFile.getPath());
        }
        ApplicationFramework.getInstance().getSerializer().saveDiagram(diagram);
    }
}

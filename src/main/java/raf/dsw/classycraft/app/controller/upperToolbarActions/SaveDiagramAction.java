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
            ApplicationFramework.getInstance().getMessageGenerator().generateMessage("You need to select a Diagram!", MessageType.ERROR);
            return;
        }

        Diagram diagram = (Diagram) MainFrame.getInstance().getClassyTree().getSelectedNode().getClassyNode();
        File diagramFile = null;

        if (diagram.getPath() == null || diagram.getPath().isEmpty()) {
            JFileChooser jFileChooser = new JFileChooser();
            if (jFileChooser.showSaveDialog(MainFrame.getInstance()) == JFileChooser.APPROVE_OPTION) {
                diagramFile = jFileChooser.getSelectedFile();
                diagram.setPath(diagramFile.getPath());
            } else {
                return; // User cancelled
            }
        }

        ApplicationFramework.getInstance().getSerializer().saveDiagram(diagram);
//        System.out.println("Diagram saved successfully.");
    }
}

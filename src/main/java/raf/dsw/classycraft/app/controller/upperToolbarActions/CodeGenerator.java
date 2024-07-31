package raf.dsw.classycraft.app.controller.upperToolbarActions;

import raf.dsw.classycraft.app.controller.AbstractClassyAction;
import raf.dsw.classycraft.app.gui.swing.view.MainFrame;
import raf.dsw.classycraft.app.model.diagramElements.connections.Connection;
import raf.dsw.classycraft.app.model.diagramElements.elements.ApstraktnaKlasa;
import raf.dsw.classycraft.app.model.diagramElements.elements.Enum;
import raf.dsw.classycraft.app.model.diagramElements.elements.Interfejs;
import raf.dsw.classycraft.app.model.diagramElements.elements.Klasa;
import raf.dsw.classycraft.app.model.modelImplementation.Diagram;
import raf.dsw.classycraft.app.model.modelImplementation.Project;
import raf.dsw.classycraft.app.model.repositoryModel.ClassyNode;
import raf.dsw.classycraft.app.model.repositoryModel.ClassyNodeComposite;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CodeGenerator extends AbstractClassyAction {
    public CodeGenerator(){
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
        putValue(SMALL_ICON, scaleIcon(loadIcon("/images/icon_deleteproject.png"), 32, 32));
        putValue(NAME, "Delete node");
        putValue(SHORT_DESCRIPTION, "Delete node");
    }
    public void actionPerformed(ActionEvent e) {

        JFileChooser jfc = new JFileChooser();
        if(!(MainFrame.getInstance().getClassyTree().getSelectedNode().getClassyNode() instanceof Project)) {
            return;
        }

        Project project = (Project) MainFrame.getInstance().getClassyTree().getSelectedNode().getClassyNode();
        if (jfc.showSaveDialog(MainFrame.getInstance()) == JFileChooser.APPROVE_OPTION) {
            File projectFolder = new File(jfc.getSelectedFile().getPath() + File.separator + project.getName());
            projectFolder.mkdirs();

            if(!project.getChildren().isEmpty()) {
                for (ClassyNode paketi : project.getChildren()) {
                    File packageFolder = new File(projectFolder.getPath() + File.separator + paketi.getName());
                    packageFolder.mkdirs();

                    if (!((ClassyNodeComposite) paketi).getChildren().isEmpty()) {
                        for (ClassyNode diagram : ((ClassyNodeComposite) paketi).getChildren()) {
                            File diagramFolder = new File(packageFolder.getPath() + File.separator + diagram.getName());
                            diagramFolder.mkdirs();

                            if (!((Diagram) diagram).getChildren().isEmpty()) {
                                for (ClassyNode diagramElement : ((Diagram) diagram).getChildren()) {
                                    if(diagramElement instanceof Connection)
                                        return;

                                    File diagramElementFile = new File(diagramFolder,diagramElement.getName() + ".txt");

                                    try (FileWriter writer = new FileWriter(diagramElementFile)) {
                                        if(diagramElement instanceof Klasa){
                                            writeExport(diagramElement, writer);
                                            writer.write(((Klasa)diagramElement).export());
                                        }
                                        else if(diagramElement instanceof Interfejs){
                                            writeExport(diagramElement, writer);
                                            writer.write(((Interfejs)diagramElement).export());
                                        }
                                        else if(diagramElement instanceof Enum){
                                            writeExport(diagramElement, writer);
                                            writer.write(((Enum)diagramElement).export());
                                        }
                                        else if(diagramElement instanceof ApstraktnaKlasa){
                                            writeExport(diagramElement, writer);
                                            writer.write(((ApstraktnaKlasa) diagramElement).export());
                                        }
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void writeExport(ClassyNode diagramElement, FileWriter writer) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(diagramElement.getParent().getParent().getParent()).append(".");
        sb.append(diagramElement.getParent().getParent()).append(".");
        sb.append(diagramElement.getParent()).append(";");
        writer.write("package ");
        writer.write(sb.toString());
        writer.write("\n");
    }
}

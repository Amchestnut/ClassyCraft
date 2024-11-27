package raf.dsw.classycraft.app.gui.swing.tree.view;

import raf.dsw.classycraft.app.gui.swing.tree.model.ClassyTreeItem;
import raf.dsw.classycraft.app.model.diagramElements.DiagramElement;
import raf.dsw.classycraft.app.model.diagramElements.elements.Interclass;
import raf.dsw.classycraft.app.model.modelImplementation.Diagram;
import raf.dsw.classycraft.app.model.modelImplementation.Package;
import raf.dsw.classycraft.app.model.modelImplementation.Project;
import raf.dsw.classycraft.app.model.modelImplementation.ProjectExplorer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

// Here we deal with the whole Tree look
// Based on the type (project, package, diagram) it will have the right image
public class ClassyTreeCellRenderer extends DefaultTreeCellRenderer {

    private static final int DESIRED_SIZE = 20;

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

        super.getTreeCellRendererComponent(tree, value, sel,expanded, leaf, row, hasFocus);
        URL imageURL = null;

        if (((ClassyTreeItem)value).getClassyNode() instanceof ProjectExplorer) {
            imageURL = getClass().getResource("/images/icon_project_explorer.png");
        }
        else if (((ClassyTreeItem)value).getClassyNode() instanceof Project) {
            imageURL = getClass().getResource("/images/icon_project.png");
        }
        else if (((ClassyTreeItem)value).getClassyNode() instanceof Package) {
            imageURL = getClass().getResource("/images/icon_package.png");
        }
        else if (((ClassyTreeItem)value).getClassyNode() instanceof Diagram) {
            imageURL = getClass().getResource("/images/icon_classdiagram.png");
        }
        else if (((ClassyTreeItem)value).getClassyNode() instanceof DiagramElement) {
            if(((ClassyTreeItem)value).getClassyNode() instanceof Interclass){
                imageURL = getClass().getResource("/images/icon_diagramelement.png");
            }
            else{
                imageURL = getClass().getResource("/images/icon_connection.png");
            }
        }

        Icon icon = null;
        if (imageURL != null) {
            try {
                BufferedImage originalImage = ImageIO.read(imageURL);
                if (originalImage != null) {
                    Image scaledImage = originalImage.getScaledInstance(DESIRED_SIZE, DESIRED_SIZE, Image.SCALE_SMOOTH);
                    icon = new ImageIcon(scaledImage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        setIcon(icon);
        return this;
    }
}

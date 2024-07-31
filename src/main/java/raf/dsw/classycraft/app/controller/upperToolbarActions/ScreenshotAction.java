package raf.dsw.classycraft.app.controller.upperToolbarActions;

import raf.dsw.classycraft.app.controller.AbstractClassyAction;
import raf.dsw.classycraft.app.gui.swing.view.DiagramView;
import raf.dsw.classycraft.app.gui.swing.view.MainFrame;
import raf.dsw.classycraft.app.gui.swing.view.PackageView;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ScreenshotAction extends AbstractClassyAction {
    public ScreenshotAction() {
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
        putValue(SMALL_ICON, scaleIcon(loadIcon("/images/screenshot.png"), 32, 32));
        putValue(NAME, "Take a screenshot of the diagram View");
        putValue(SHORT_DESCRIPTION, "Take a screenshot of the diagram View");
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        PackageView packageView = MainFrame.getInstance().getPackageView();
        JScrollPane jScrollPane = (JScrollPane) packageView.getTabbedPane().getSelectedComponent();
        DiagramView diagramView = packageView.getDiagramViewFromScrollPane(jScrollPane);

        Rectangle viewRect = jScrollPane.getViewport().getViewRect();                                           // ONLY the visible part (from viewport)

        BufferedImage image = new BufferedImage(viewRect.width, viewRect.height, BufferedImage.TYPE_INT_ARGB);  // create an img of the size of the visible rectangle
        Graphics2D g2d = image.createGraphics();                                                                // Use the DiagramView's paint method to draw only the visible part onto the image's graphics context - andrija

        g2d.translate(-viewRect.x, -viewRect.y);                                                                // pomeramo offset diagramView, da bi uhvatili tacan deo
        diagramView.paint(g2d);
        g2d.dispose();

        JFileChooser jFileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG files", "png");
        jFileChooser.setFileFilter(filter);

        if (jFileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File filePicture = jFileChooser.getSelectedFile();

            if (!filePicture.getName().toLowerCase().endsWith(".png"))
                filePicture = new File(filePicture.getAbsolutePath() + ".png");

            try {
                ImageIO.write(image, "png", filePicture);
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
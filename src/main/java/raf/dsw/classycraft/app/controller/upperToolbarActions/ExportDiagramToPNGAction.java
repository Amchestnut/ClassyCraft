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

public class ExportDiagramToPNGAction extends AbstractClassyAction {
    public ExportDiagramToPNGAction() {
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
        putValue(SMALL_ICON, scaleIcon(loadIcon("/images/export_to_png.png"), 32, 32));
        putValue(NAME, "Export whole diagram to PNG");
        putValue(SHORT_DESCRIPTION, "Export whole diagram to PNG");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        PackageView packageView = MainFrame.getInstance().getPackageView();
        JScrollPane jScrollPane = (JScrollPane) packageView.getTabbedPane().getSelectedComponent();
        DiagramView diagramView = packageView.getDiagramViewFromScrollPane(jScrollPane);

        JFileChooser jFileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG files", "png");
        jFileChooser.setFileFilter(filter);

        BufferedImage image = (BufferedImage) diagramView.createImage(diagramView.getWidth(), diagramView.getHeight());

        Graphics2D g2d = image.createGraphics();
        diagramView.paint(g2d);
        g2d.dispose();
        System.out.println(image);

        if (jFileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File filePicture = jFileChooser.getSelectedFile();

            if (!filePicture.getName().toLowerCase().endsWith(".png")) {
                filePicture = new File(filePicture.getAbsolutePath() + ".png");
            }
            try {
                ImageIO.write(image, "png", filePicture);
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}

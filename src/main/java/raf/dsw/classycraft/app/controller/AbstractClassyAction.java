package raf.dsw.classycraft.app.controller;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public abstract class AbstractClassyAction extends AbstractAction {
    public ImageIcon loadIcon(String fileName) {

        URL imageURL = getClass().getResource(fileName);
        ImageIcon icon = null;

        if (imageURL != null) {
            icon = new ImageIcon(imageURL);
        } else {
            System.err.println("File " + fileName + " not found");
        }
        return icon;
    }

    public ImageIcon scaleIcon(ImageIcon originalIcon, int width, int height) {
        if (originalIcon != null) {
            Image originalImage = originalIcon.getImage();
            Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }
        return null;
    }
}
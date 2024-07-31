package raf.dsw.classycraft.app.gui.swing.view;

import javax.swing.*;
import java.awt.*;

public class AboutUsFrame extends JFrame {
    private JLabel imeIgor;
    private JLabel imeAndrija;
    private ImageIcon imageIgor;
    private ImageIcon imageAndrija;

    public AboutUsFrame(ImageIcon imageAndrija, ImageIcon imageIgor){
        initialise(imageAndrija, imageIgor);
        positionalise();
    }
    private void initialise(ImageIcon imageAndrija, ImageIcon imageIgor){
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        setSize(screenWidth / 3, screenHeight / 3);
        setLocationRelativeTo(null);
        setTitle("About us");
        this.setVisible(true);

        this.imageIgor = imageIgor;
        this.imageAndrija = imageAndrija;

        imeAndrija = new JLabel("Andrija Milikic");
        imeIgor = new JLabel("Igor Gajic");
    }
    private void positionalise(){
        //Stavljamo layout
        this.setLayout(new GridLayout(2, 2));
        //Centriramo imena
        imeIgor.setHorizontalAlignment(SwingConstants.CENTER);
        imeAndrija.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(imeIgor);
        this.add(imeAndrija);

        //Dodajemo slike
        JLabel labelImageAndrija = new JLabel(imageAndrija);
        JLabel labelImageIgor = new JLabel(imageIgor);
        this.add(labelImageIgor);
        this.add(labelImageAndrija);
    }
}

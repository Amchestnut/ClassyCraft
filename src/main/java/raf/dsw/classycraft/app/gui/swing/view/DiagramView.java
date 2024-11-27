package raf.dsw.classycraft.app.gui.swing.view;

import raf.dsw.classycraft.app.controller.commandActions.CommandManager;
import raf.dsw.classycraft.app.controller.mouseAdapters.MouseAdapter;
import raf.dsw.classycraft.app.gui.swing.painters.ElementPainter;
import raf.dsw.classycraft.app.gui.swing.painters.elementi.*;
import raf.dsw.classycraft.app.gui.swing.painters.veze.ConnectionPainter;
import raf.dsw.classycraft.app.gui.swing.tree.model.ClassyTreeItem;
import raf.dsw.classycraft.app.model.diagramElements.DiagramElement;
import raf.dsw.classycraft.app.model.diagramElements.connections.DataForConnection;
import raf.dsw.classycraft.app.model.diagramElements.elements.DataForElementFromDialog;
import raf.dsw.classycraft.app.model.diagramElements.elements.Interclass;
import raf.dsw.classycraft.app.model.modelImplementation.Diagram;
import raf.dsw.classycraft.app.model.repositoryModel.ClassyNode;
import raf.dsw.classycraft.app.notifications.NotificationForAddingAndRemoving;
import raf.dsw.classycraft.app.notifications.NotificationForChangingName;
import raf.dsw.classycraft.app.notifications.NotificationForResettingCommandManager;
import raf.dsw.classycraft.app.observer.ISubscriber;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DiagramView extends JPanel implements ISubscriber {

    private String tabName;
    private List<ElementPainter> painters;
    private List<ElementPainter> selectionedRectangles;             // lista paintera, jer painter ima model , a model nema painter ;)
    private Diagram diagram;
    private Rectangle selectionRectangle = null;                    // pravougaonik za selekciju
    private Line2D temporaryLine = null;
    private List<ElementPainter> selectionedConnections;
    private AffineTransform transform = new AffineTransform();
    private CommandManager commandManager;

    public DiagramView(String tabName, Diagram diagram) {
        this.tabName = tabName;
        this.diagram = diagram;
        painters = new ArrayList<>();
        selectionedRectangles = new ArrayList<>();
        selectionedConnections = new ArrayList<>();
        commandManager = new CommandManager();

        initializeView();
    }

    private void initializeView() {
        MouseAdapter mouseAdapter = new MouseAdapter(this);
        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);
    }

    @Override
    public void update(Object notification) {

        if (notification instanceof NotificationForChangingName) {
            this.tabName = ((NotificationForChangingName) notification).getName();
        }
        if(notification instanceof NotificationForAddingAndRemoving) {
            if(((NotificationForAddingAndRemoving) notification).getType().equalsIgnoreCase("REMOVE")){
                DiagramElement diagramElement = (DiagramElement) ((NotificationForAddingAndRemoving) notification).getNode();
                Iterator<ElementPainter> iterator = painters.iterator();
                while(iterator.hasNext()){
                    ElementPainter ep = iterator.next();
                    if(ep instanceof InterclassPainter){
                        if(ep.getDiagramElement().equals(diagramElement)){
                            iterator.remove(); // koristim iterator da bi izbacio element
                        }
                    }
                }
            }
            if(((NotificationForAddingAndRemoving) notification).getType().equalsIgnoreCase("remove connection")){
                DiagramElement connectionElement = (DiagramElement) ((NotificationForAddingAndRemoving) notification).getNode();
                Iterator<ElementPainter> iterator = painters.iterator();
                while(iterator.hasNext()){
                    ElementPainter ep = iterator.next();
                    if(ep instanceof ConnectionPainter){
                        if(ep.getDiagramElement().equals(connectionElement)){
                            iterator.remove(); // removing the connection painter
                        }
                    }
                }
            }
        }
        if(notification instanceof NotificationForResettingCommandManager){
            Diagram node = ((NotificationForResettingCommandManager) notification).getDiagram();
            if(node.equals(diagram))
                setCommandManager(new CommandManager());
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.transform(transform);
        for (ElementPainter painter : painters) {
            painter.paint(g2d);
        }

        if (selectionRectangle != null) {
            float[] dash1 = {10.0f};            // 10 pixela je dash
            BasicStroke dashed =
                    new BasicStroke(1.0f,
                            BasicStroke.CAP_BUTT,
                            BasicStroke.JOIN_MITER,
                            10.0f, dash1, 0.0f); // ovde pravim stroke, i stavljam mu ove osobine

            g2d.setStroke(dashed);
            g2d.setColor(Color.BLACK);
            g2d.draw(selectionRectangle);
        }
        if (temporaryLine != null) {
            g2d.setColor(new Color(0, 0, 0));
            g2d.draw(temporaryLine);
        }
    }

    public DataForElementFromDialog kojuInterklasuHoceUser() {
        JDialog dialog = new JDialog((Frame) null, "Create Element", true);
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));

        DataForElementFromDialog dataForElementFromDialog = new DataForElementFromDialog();         // OBJECT u kome cuvam ukucane podatke

        JTextField nameField = new JTextField(20);
        dialog.add(new JLabel("Name:"));
        dialog.add(nameField);

        ButtonGroup typeGroup = new ButtonGroup();
        JRadioButton classButton = new JRadioButton("Class");
        JRadioButton abstractClassButton = new JRadioButton("Abstract Class");
        JRadioButton interfaceButton = new JRadioButton("Interface");
        JRadioButton enumButton = new JRadioButton("Enum Class");

        typeGroup.add(classButton);
        typeGroup.add(abstractClassButton);
        typeGroup.add(interfaceButton);
        typeGroup.add(enumButton);

        JPanel radioPanel = new JPanel(new FlowLayout());
        radioPanel.add(classButton);
        radioPanel.add(abstractClassButton);
        radioPanel.add(interfaceButton);
        radioPanel.add(enumButton);
        dialog.add(radioPanel);

        classButton.setSelected(true);

        // text area za atribute i metode
        JTextArea attributesArea = new JTextArea(5, 20);
        attributesArea.setBorder(BorderFactory.createTitledBorder("Attributes"));
        dodajHintText("-Uneti atribute u sledecem formatu: \n" +
                "-vidljivost, naziv atributa, povratni tip, kao na primer: + \n" +
                "-brojStrana: int                         ili:\n" +
                "+nazivKnjige: String                 ili:\n" +
                "#debljinaStrane: double           ili:\n" +
                "~procitanaKnjiga: boolean"
                , attributesArea);


        JTextArea methodsArea = new JTextArea(5, 20);
        methodsArea.setBorder(BorderFactory.createTitledBorder("Methods"));
        dodajHintText("-Uneti metode u sledecem formatu:  \n" +
                        "-nazivMetode (tip argumenata, npr int, ili String,int,int): povratni tip, kao na primer: \n"  +
                        "+izracunajBrojSlovaNaStrani (int): int              ili:\n" +
                        "#kojiDanJeDanas: (String): String                   ili:\n" +
                        "~daLiJeProcitao: (int): boolean"
                , methodsArea);


        // ne mogu da se dodaju atributi u interfejs i metode u enum
        ItemListener itemListener = e -> {
            attributesArea.setEnabled(!interfaceButton.isSelected());
            methodsArea.setEnabled(!enumButton.isSelected());
        };

        classButton.addItemListener(itemListener);
        abstractClassButton.addItemListener(itemListener);
        interfaceButton.addItemListener(itemListener);
        enumButton.addItemListener(itemListener);

        JScrollPane attributesScrollPane = new JScrollPane(attributesArea);
        JScrollPane methodsScrollPane = new JScrollPane(methodsArea);
        dialog.add(attributesScrollPane);
        dialog.add(methodsScrollPane);

        JButton AddButton = new JButton("ADD");
        dialog.add(AddButton);
        dialog.pack();                                              // size ovaj dialog
        dialog.setLocationRelativeTo(null);                         // centriraj
        final boolean[] addButtonPressed = {false};


        // kada kliknem ok, pokupi sve podatke
        AddButton.addActionListener(e -> {

            String selectedType;
            if (classButton.isSelected()) {
                selectedType = "Class";
            }
            else if (abstractClassButton.isSelected()) {
                selectedType = "Abstract Class";
            }
            else if (interfaceButton.isSelected()) {
                selectedType = "Interface";
            }
            else if (enumButton.isSelected()) {
                selectedType = "Enum Class";
            }
            else {
                selectedType = "None"; // this case should not happen as one is always selected
            }

            // !enumButton.isSelected() &&
            if(attributesArea.getText().equalsIgnoreCase("-Uneti atribute u sledecem formatu: \n" + "-vidljivost, naziv atributa, povratni tip, kao na primer: + \n" + "-brojStrana: int                         ili:\n" + "+nazivKnjige: String                 ili:\n" + "#debljinaStrane: double           ili:\n" + "~procitanaKnjiga: boolean")){
                attributesArea.setText("");
            }
            if(methodsArea.getText().equalsIgnoreCase("-Uneti metode u sledecem formatu:  \n" + "-nazivMetode (tip argumenata, npr int, ili String,int,int): povratni tip, kao na primer: \n"  + "+izracunajBrojSlovaNaStrani (int): int              ili:\n" + "#kojiDanJeDanas: (String): String                   ili:\n" + "~daLiJeProcitao: (int): boolean")){
                methodsArea.setText("");
                //JOptionPane.showMessageDialog(dialog, "Ne mozete uneti hint text kao metodu! Unesite svoje metode!", "Input Error", JOptionPane.ERROR_MESSAGE);
                //return;
            }

            String[] sviAtributi = attributesArea.getText().split("\n");
            boolean atributiOK = true;
            // proveravamo sve unete atribute, osim ako nije u pitanju interfejs
            if(!interfaceButton.isSelected()){
                for(String atribut: sviAtributi){
                    if(proveriNetacnostUnosaZaAtribut(atribut)){         // ako vrati false, atributi nisu ok
                        atributiOK = false;
                    }
                }
            }

            String[] sveMetode = methodsArea.getText().split("\n");
            boolean metodeOK = true;
            // proveravamo sve unete metode, osim ako nije u pitanju enum
            if(!enumButton.isSelected()){
                for(String metoda: sveMetode){
                    if(proveriNetacnostUnosaZaMetodu(metoda)){
                        metodeOK = false;
                    }
                }
            }

            String vidlivostKlase = nameField.getText();
            boolean checkIfEnteredVisibility = vidlivostKlase.isEmpty() || vidlivostKlase.charAt(0) == '+' || vidlivostKlase.charAt(0) == '-' || vidlivostKlase.charAt(0) == '#' || vidlivostKlase.charAt(0) == '~';
            if(!checkIfEnteredVisibility){
                JOptionPane.showMessageDialog(dialog, "Nije tacno uneta vidljivost klase", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!atributiOK) {
                JOptionPane.showMessageDialog(dialog, "Nije tacno unet atribut", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!metodeOK) {
                JOptionPane.showMessageDialog(dialog, "Nije tacno uneta metoda", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if(interfaceButton.isSelected())
                attributesArea.setText("");
            if(enumButton.isSelected())
                methodsArea.setText("");

            dataForElementFromDialog.setName(nameField.getText());
            dataForElementFromDialog.setType(selectedType);
            dataForElementFromDialog.setAttributes(attributesArea.getText());
            dataForElementFromDialog.setMethods(methodsArea.getText());

            addButtonPressed[0] = true;
            dialog.dispose();
        });


        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                addButtonPressed[0] = false;
            }
        });
        dialog.setVisible(true);

        if (!addButtonPressed[0]) {
            this.getSelectionedRectangles().clear();
            backToOriginalColor();
            return null;
        }
        return dataForElementFromDialog;
    }

    public DataForElementFromDialog showEditDialogForInterclass(DataForElementFromDialog preFilledData) {
        JDialog dialog = new JDialog((Frame) null,"Edit Element", true);
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));

        JTextField nameField = new JTextField(preFilledData.getName(), 20);
        dialog.add(new JLabel("Name:"));
        dialog.add(nameField);

        JRadioButton classButton = new JRadioButton("Class");
        JRadioButton abstractClassButton = new JRadioButton("Abstract Class");
        JRadioButton interfaceButton = new JRadioButton("Interface");
        JRadioButton enumButton = new JRadioButton("Enum Class");

        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(classButton);
        typeGroup.add(abstractClassButton);
        typeGroup.add(interfaceButton);
        typeGroup.add(enumButton);

        JPanel radioPanel = new JPanel(new FlowLayout());
        radioPanel.add(classButton);
        radioPanel.add(abstractClassButton);
        radioPanel.add(interfaceButton);
        radioPanel.add(enumButton);
        dialog.add(radioPanel);

        String typeOfTheSelectedInterclassWas = preFilledData.getType();
        if(typeOfTheSelectedInterclassWas.equalsIgnoreCase("Class")){
            classButton.setSelected(true);
        }
        else if(typeOfTheSelectedInterclassWas.equalsIgnoreCase("Abstract Class")){
            abstractClassButton.setSelected(true);
        }
        else if(typeOfTheSelectedInterclassWas.equalsIgnoreCase("Interface")){
            interfaceButton.setSelected(true);
        }
        else if(typeOfTheSelectedInterclassWas.equalsIgnoreCase("Enum Class")){
            enumButton.setSelected(true);
        }

        JTextArea attributesArea = new JTextArea(preFilledData.getAttributes(), 5, 20);
        attributesArea.setBorder(BorderFactory.createTitledBorder("Attributes"));

        JTextArea methodsArea = new JTextArea(preFilledData.getMethods(), 5, 20);
        methodsArea.setBorder(BorderFactory.createTitledBorder("Methods"));

        JScrollPane attributesScrollPane = new JScrollPane(attributesArea);
        JScrollPane methodsScrollPane = new JScrollPane(methodsArea);
        dialog.add(attributesScrollPane);
        dialog.add(methodsScrollPane);
        attributesArea.setEnabled(!typeOfTheSelectedInterclassWas.equalsIgnoreCase("Interface"));   // ne moze da se edituje textfield atributi kad je interfejs
        methodsArea.setEnabled(!typeOfTheSelectedInterclassWas.equalsIgnoreCase("Enum class"));

        ItemListener itemListener = e -> attributesArea.setEnabled(!interfaceButton.isSelected());

        classButton.addItemListener(itemListener);
        abstractClassButton.addItemListener(itemListener);
        interfaceButton.addItemListener(itemListener);
        enumButton.addItemListener(itemListener);

        classButton.setEnabled(false);
        abstractClassButton.setEnabled(false);
        interfaceButton.setEnabled(false);
        enumButton.setEnabled(false);

        JButton AddButton = new JButton("SAVE");
        dialog.add(AddButton);
        dialog.pack();
        dialog.setLocationRelativeTo(null);

        attributesArea.setEnabled(!interfaceButton.isSelected());
        methodsArea.setEnabled(!enumButton.isSelected());

        final boolean[] addButtonPressed = {false};

        AddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String selectedType;
                if (classButton.isSelected()) {
                    selectedType = "Class";
                }
                else if (abstractClassButton.isSelected()) {
                    selectedType = "Abstract Class";
                }
                else if (interfaceButton.isSelected()) {
                    selectedType = "Interface";
                }
                else if (enumButton.isSelected()) {
                    selectedType = "Enum Class";
                }
                else {
                    selectedType = "None"; // this case should not happen as one is always selected
                }

                String vidlivostKlase = nameField.getText();
                boolean checkIfEnteredVisibility = vidlivostKlase.isEmpty() || vidlivostKlase.charAt(0) == '+' || vidlivostKlase.charAt(0) == '-' || vidlivostKlase.charAt(0) == '#' || vidlivostKlase.charAt(0) == '~';

                if(!checkIfEnteredVisibility){
                    JOptionPane.showMessageDialog(dialog, "Nije tacno uneta vidljivost klase", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // !enumButton.isSelected() &&
                if(attributesArea.getText().equalsIgnoreCase("-Uneti atribute u sledecem formatu: \n" + "-vidljivost, naziv atributa, povratni tip, kao na primer: + \n" + "-brojStrana: int                         ili:\n" + "+nazivKnjige: String                 ili:\n" + "#debljinaStrane: double           ili:\n" + "~procitanaKnjiga: boolean")){
                    attributesArea.setText("");
                }
                if(methodsArea.getText().equalsIgnoreCase("-Uneti metode u sledecem formatu:  \n" + "-nazivMetode (tip argumenata, npr int, ili String,int,int): povratni tip, kao na primer: \n"  + "+izracunajBrojSlovaNaStrani (int): int              ili:\n" + "#kojiDanJeDanas: (String): String                   ili:\n" + "~daLiJeProcitao: (int): boolean")){
                    methodsArea.setText("");
                }

                String[] sviAtributi = attributesArea.getText().split("\n");
                boolean atributiOK = true;
                // proveravamo sve unete atribute, osim ako nije u pitanju interfejs
                if(!interfaceButton.isSelected()){
                    for(String atribut: sviAtributi){
                        if(proveriNetacnostUnosaZaAtribut(atribut)){         // ako vrati false, atributi nisu ok
                            atributiOK = false;
                        }
                    }
                }

                String[] sveMetode = methodsArea.getText().split("\n");
                boolean metodeOK = true;
                // proveravamo sve unete metode, osim ako nije u pitanju enum
                if(!enumButton.isSelected()){
                    for(String metoda: sveMetode){
                        if(proveriNetacnostUnosaZaMetodu(metoda)){
                            metodeOK = false;
                        }
                    }
                }

                if (!atributiOK) {
                    JOptionPane.showMessageDialog(dialog, "Nije tacno unet atribut", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!metodeOK) {
                    JOptionPane.showMessageDialog(dialog, "Nije tacno uneta metoda", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if(interfaceButton.isSelected())
                    attributesArea.setText("");
                if(enumButton.isSelected())
                    methodsArea.setText("");

                preFilledData.setName(nameField.getText());
                preFilledData.setType(selectedType);
                preFilledData.setAttributes(attributesArea.getText());
                preFilledData.setMethods(methodsArea.getText());

                addButtonPressed[0] = true;
                dialog.dispose();
            }
        });


        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                addButtonPressed[0] = false;
            }
        });

        dialog.setVisible(true);

        if (!addButtonPressed[0]) {
            return null;
        }
        return preFilledData;
    }

    public DataForConnection kojuVezuHoceUser() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Select Connection Type");
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
        dialog.setModal(true);
        dialog.setSize(600, 400);

        JRadioButton associationButton = new JRadioButton("Veza asocijacije");
        JRadioButton inheritanceButton = new JRadioButton("Veza nasledjivanja");
        JRadioButton realisationButton = new JRadioButton("Veza realizacije");
        JRadioButton dependencyButton = new JRadioButton("Veza zavisnosti");
        JRadioButton aggregationButton = new JRadioButton("Veza agregacije");
        JRadioButton compositionButton = new JRadioButton("Veza kompozicije", true);   // default upaljen

        ButtonGroup group = new ButtonGroup();
        group.add(associationButton);
        group.add(inheritanceButton);
        group.add(realisationButton);
        group.add(dependencyButton);
        group.add(aggregationButton);
        group.add(compositionButton);

        dialog.add(associationButton);
        dialog.add(inheritanceButton);
        dialog.add(realisationButton);
        dialog.add(dependencyButton);
        dialog.add(aggregationButton);
        dialog.add(compositionButton);

        JLabel labelFrom = new JLabel("Element IZ koga se povlaci veza, ima instancu elementa DO u obliku:");   // labela
        JTextArea textAreaFrom = new JTextArea(1, 20);         // samo jedan red
        textAreaFrom.setBorder(BorderFactory.createTitledBorder("Instanca interklase IZ"));
        dodajHintText("-tipKnjizevnosti 0..1", textAreaFrom);       // hint text
        JScrollPane prvi = new JScrollPane(textAreaFrom);

        JLabel labelTo = new JLabel("Element DO, u kome se zavrsava veza, ima instancu elementa IZ u obliku:");
        JTextArea textAreaTo = new JTextArea(1, 20);
        textAreaTo.setBorder(BorderFactory.createTitledBorder("Instanca interklase DO"));
        dodajHintText("-knjige 0..*", textAreaTo);
        JScrollPane drugi = new JScrollPane(textAreaTo);

        dialog.add(labelFrom);
        dialog.add(prvi);
        dialog.add(labelTo);
        dialog.add(drugi);

        // listener da enabluje ili disabluje textarea na osnovu toga koji button je kliknut
        ActionListener al = e -> {
            boolean enable = associationButton.isSelected() || aggregationButton.isSelected() || compositionButton.isSelected();
            textAreaFrom.setEnabled(enable);
            textAreaTo.setEnabled(enable);
        };

        associationButton.addActionListener(al);
        inheritanceButton.addActionListener(al);
        realisationButton.addActionListener(al);
        dependencyButton.addActionListener(al);
        aggregationButton.addActionListener(al);
        compositionButton.addActionListener(al);

        final boolean[] okPressed = {false};
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {

            String enteredText1 = textAreaFrom.getText();
            String enteredText2 = textAreaTo.getText();

            boolean firstText = proveriTacnostUnosaZaVezu(enteredText1);
            boolean secondText= proveriTacnostUnosaZaVezu(enteredText2);

            boolean prviJePrazan = false;
            boolean drugiJePrazan = false;

            if(enteredText1.equalsIgnoreCase("")){
                firstText = true;
                prviJePrazan = true;
            }
            if(enteredText2.equalsIgnoreCase("")){
                secondText = true;
                drugiJePrazan = true;
            }
            boolean obaSuPrazna = false;
            if(associationButton.isSelected() || aggregationButton.isSelected() || compositionButton.isSelected()){
                obaSuPrazna = prviJePrazan && drugiJePrazan;        // ne smeju oba da budu prazna, ali smeju ako je neka druga veza

                if(enteredText1.equalsIgnoreCase("-tipKnjizevnosti 0..1") || enteredText2.equalsIgnoreCase("-knjige 0..*")){
                    JOptionPane.showMessageDialog(dialog, "Ne moze da ostane default text", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            if(associationButton.isSelected() || aggregationButton.isSelected() || compositionButton.isSelected()){
                boolean result = firstText && secondText && (!obaSuPrazna);
                if(!result){
                    JOptionPane.showMessageDialog(dialog, "Pogresan unos! Ne smeju oba da budu prazna, ili je neki unos neispravan", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }


            okPressed[0] = true;
            dialog.dispose();
        });

        dialog.add(okButton);

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        if (!okPressed[0]) {
            return null;                // dialog zatvoren a nije kliknut ok
        }

        // Extractujem selected values
        String connectionType = null;

        if (associationButton.isSelected()) {
            connectionType = "asocijacija";
        }
        else if (inheritanceButton.isSelected()) {
            connectionType = "nasledjivanje";
        }
        else if (realisationButton.isSelected()) {
            connectionType = "realizacija";
        }
        else if (dependencyButton.isSelected()) {
            connectionType = "zavisnost";
        }
        else if (aggregationButton.isSelected()) {
            connectionType = "agregacija";
        }
        else if (compositionButton.isSelected()) {
            connectionType = "kompozicija";
        }

        DataForConnection dataForConnection = new DataForConnection(connectionType);

        // ako je selektovana veza asocijacije, agregacije ili kompozicije, dodaj mu i ostale parametre osim tipa
        if(associationButton.isSelected() || aggregationButton.isSelected() || compositionButton.isSelected()){
            String firstText = textAreaFrom.getText();
            String secondText = textAreaTo.getText();

            if(!firstText.equalsIgnoreCase("")){                  // ako nije empty
                String[] parts1 = firstText.trim().split("\\s+");
                String visibilityFrom = parts1[0].substring(0, 1);           // prvi karakter (vidljivost)
                String instanceFrom = parts1[0].substring(1);      // instanca

                dataForConnection.setVisibilityOfTheFirstElement(visibilityFrom);  // lepo napravljen custom Data Object koji prima ovo
                dataForConnection.setInstanceOfTheFirstElement(instanceFrom);
                dataForConnection.setKardinalnostOfTheFirstElement(parts1[1]);
            }

            if(!secondText.equalsIgnoreCase("")){
                String[] parts2 = secondText.trim().split("\\s+");
                String visibilityTo = parts2[0].substring(0, 1);
                String instanceTo = parts2[0].substring(1);

                dataForConnection.setVisibilityOfTheSecondElement(visibilityTo);
                dataForConnection.setInstanceOfTheSecondElement(instanceTo);
                dataForConnection.setKardinalnostOfTheSecondElement(parts2[1]);
            }
        }
        return dataForConnection;
    }

    public DataForConnection showEditDialogForConnections(DataForConnection preFilledData) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Select Connection Type");
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
        dialog.setModal(true);
        dialog.setSize(600, 400);

        JRadioButton associationButton = new JRadioButton("Veza asocijacije");
        JRadioButton inheritanceButton = new JRadioButton("Veza nasledjivanja");
        JRadioButton realisationButton = new JRadioButton("Veza realizacije");
        JRadioButton dependencyButton = new JRadioButton("Veza zavisnosti");
        JRadioButton aggregationButton = new JRadioButton("Veza agregacije");
        JRadioButton compositionButton = new JRadioButton("Veza kompozicije");

        String tip = preFilledData.getType();
        if(tip.equalsIgnoreCase("asocijacija")){
            associationButton.setSelected(true);
        }
        else if(tip.equalsIgnoreCase("nasledjivanje")){
            inheritanceButton.setSelected(true);
        }
        else if(tip.equalsIgnoreCase("realizacija")){
            realisationButton.setSelected(true);
        }
        else if(tip.equalsIgnoreCase("zavisnost")){
            dependencyButton.setSelected(true);
        }
        else if(tip.equalsIgnoreCase("agregacija")){
            aggregationButton.setSelected(true);
        }
        else if(tip.equalsIgnoreCase("kompozicija")){
            compositionButton.setSelected(true);
        }

        associationButton.setEnabled(false);
        inheritanceButton.setEnabled(false);
        realisationButton.setEnabled(false);
        dependencyButton.setEnabled(false);
        aggregationButton.setEnabled(false);
        compositionButton.setEnabled(false);

        ButtonGroup group = new ButtonGroup();
        group.add(associationButton);
        group.add(inheritanceButton);
        group.add(realisationButton);
        group.add(dependencyButton);
        group.add(aggregationButton);
        group.add(compositionButton);

        dialog.add(associationButton);
        dialog.add(inheritanceButton);
        dialog.add(realisationButton);
        dialog.add(dependencyButton);
        dialog.add(aggregationButton);
        dialog.add(compositionButton);

        String unosIzPrvogTextField = "";
        unosIzPrvogTextField += preFilledData.getVisibilityOfTheFirstElement() + preFilledData.getInstanceOfTheFirstElement() + " " + preFilledData.getKardinalnostOfTheFirstElement();

        String unosIzDrugogTextField = "";
        unosIzDrugogTextField = preFilledData.getVisibilityOfTheSecondElement() + preFilledData.getInstanceOfTheSecondElement() + " " + preFilledData.getKardinalnostOfTheSecondElement();

        JLabel labelFrom = new JLabel("Element IZ koga se povlaci veza, ima instancu elementa DO u obliku:");   // labela
        JTextArea textAreaFrom = new JTextArea(1, 20);
        textAreaFrom.setBorder(BorderFactory.createTitledBorder("Instanca interklase IZ"));
        textAreaFrom.setText(unosIzPrvogTextField);
        JScrollPane prvi = new JScrollPane(textAreaFrom);

        JLabel labelTo = new JLabel("Element DO, u kome se zavrsava veza, ima instancu elementa IZ u obliku:");
        JTextArea textAreaTo = new JTextArea(1, 20);
        textAreaTo.setBorder(BorderFactory.createTitledBorder("Instanca interklase DO"));
        textAreaTo.setText(unosIzDrugogTextField);
        JScrollPane drugi = new JScrollPane(textAreaTo);

        dialog.add(labelFrom);
        dialog.add(prvi);
        dialog.add(labelTo);
        dialog.add(drugi);

        if(tip.equalsIgnoreCase("nasledjivanje") || tip.equalsIgnoreCase("realizacija") || tip.equalsIgnoreCase("zavisnost")){
            textAreaFrom.setEditable(false);
            textAreaTo.setEditable(false);
        }

        final boolean[] okPressed = {false};
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {

            String enteredText1 = textAreaFrom.getText();
            String enteredText2 = textAreaTo.getText();

            boolean firstText = proveriTacnostUnosaZaVezu(enteredText1);
            boolean secondText= proveriTacnostUnosaZaVezu(enteredText2);

            boolean prviJePrazan = false;
            boolean drugiJePrazan = false;

            if(enteredText1.equalsIgnoreCase("")){
                firstText = true;
                prviJePrazan = true;
            }
            if(enteredText2.equalsIgnoreCase("")){
                secondText = true;
                drugiJePrazan = true;
            }
            boolean obaSuPrazna = false;
            if(associationButton.isSelected() || aggregationButton.isSelected() || compositionButton.isSelected()){
                obaSuPrazna = prviJePrazan && drugiJePrazan;        // ne smeju oba da budu prazna, ali smeju ako je neka druga veza


                if(enteredText1.equalsIgnoreCase("-tipKnjizevnosti 0..1") || enteredText2.equalsIgnoreCase("-knjige 0..*")){
                    JOptionPane.showMessageDialog(dialog, "Ne moze da ostane default text", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if(associationButton.isSelected() || aggregationButton.isSelected() || compositionButton.isSelected()){
                boolean result = firstText && secondText && (!obaSuPrazna);
                if(!result){
                    JOptionPane.showMessageDialog(dialog, "Pogresan unos! Ne smeju oba da budu prazna, ili je neki unos neispravan", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }


            okPressed[0] = true;
            dialog.dispose();
        });

        dialog.add(okButton);

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        if (!okPressed[0]) {
            return null;                // dialog zatvoren a nije kliknut ok
        }

        // Extractujem selected values
        String connectionType = null;

        if (associationButton.isSelected()) {
            connectionType = "asocijacija";
        }
        else if (inheritanceButton.isSelected()) {
            connectionType = "nasledjivanje";
        }
        else if (realisationButton.isSelected()) {
            connectionType = "realizacija";
        }
        else if (dependencyButton.isSelected()) {
            connectionType = "zavisnost";
        }
        else if (aggregationButton.isSelected()) {
            connectionType = "agregacija";
        }
        else if (compositionButton.isSelected()) {
            connectionType = "kompozicija";
        }

        DataForConnection dataForConnection = new DataForConnection(connectionType);

        // ako je selektovana veza asocijacije, agregacije ili kompozicije, dodaj mu i ostale parametre osim tipa
        if(associationButton.isSelected() || aggregationButton.isSelected() || compositionButton.isSelected()){
            String firstText = textAreaFrom.getText();
            String secondText = textAreaTo.getText();

            if(!firstText.equalsIgnoreCase("")){                            // ako nije empty
                String[] parts1 = firstText.trim().split("\\s+");
                String visibilityFrom = parts1[0].substring(0, 1);                     // prvi karakter (vidljivost)
                String instanceFrom = parts1[0].substring(1);                // instanca

                dataForConnection.setVisibilityOfTheFirstElement(visibilityFrom);      // lepo napravljen custom Data Object koji prima ovo
                dataForConnection.setInstanceOfTheFirstElement(instanceFrom);
                dataForConnection.setKardinalnostOfTheFirstElement(parts1[1]);
            }

            if(!secondText.equalsIgnoreCase("")){
                String[] parts2 = secondText.trim().split("\\s+");
                String visibilityTo = parts2[0].substring(0, 1);
                String instanceTo = parts2[0].substring(1);

                dataForConnection.setVisibilityOfTheSecondElement(visibilityTo);
                dataForConnection.setInstanceOfTheSecondElement(instanceTo);
                dataForConnection.setKardinalnostOfTheSecondElement(parts2[1]);
            }

        }

        return dataForConnection;
    }

    public void backToOriginalColor(){

        for(ElementPainter painter : painters){
            if(painter instanceof KlasaPainter){
                DiagramElement diagramElement = ((KlasaPainter)painter).getDiagramElement();
                diagramElement.setColor(0xFFFF4040); // malo svetlija boja od originalne crvene
            }
            else if(painter instanceof InterfejsPainter){
                DiagramElement diagramElement = ((InterfejsPainter)painter).getDiagramElement();
                diagramElement.setColor(0xFF40FF40);
            }
            else if(painter instanceof AbstractClassPainter){
                DiagramElement diagramElement = ((AbstractClassPainter)painter).getDiagramElement();
                diagramElement.setColor(0xFFFFFF40);
            }
            else if(painter instanceof EnumPainter){
                DiagramElement diagramElement = ((EnumPainter)painter).getDiagramElement();
                diagramElement.setColor(0xFF4040FF);
            }

            if(painter instanceof ConnectionPainter){
                DiagramElement diagramElement = ((ConnectionPainter) painter).getDiagramElement();
                diagramElement.setColor(0xFF000000);
            }
        }
    }

    public void dodajMeKaoSubscribera(ClassyNode node){
        if(node instanceof DiagramElement){
            ((DiagramElement) node).addSubscriber(this);
        }
    }

    public boolean proveriTacnostUnosaZaVezu(String unos){

        String[] parts = unos.trim().split("\\s+");   // splitujemo po space

        if (parts.length != 2)                              // ako ima vise od 2 dela, vrati false i reci neispravan unos
            return false;

        // samo provera da li prvi karakter pocinje sa -,+,~
        boolean firstPartValid = parts[0].length() > 0 &&
                       (parts[0].charAt(0) == '+' ||
                        parts[0].charAt(0) == '-' ||
                        parts[0].charAt(0) == '#' ||
                        parts[0].charAt(0) == '~');

        boolean secondPartValid = parts[1].equals("0..1") || parts[1].equals("0..*");

        return firstPartValid && secondPartValid;
    }

    public boolean proveriNetacnostUnosaZaAtribut(String unos){
        if(unos.equalsIgnoreCase(""))
            return false;

        String[] parts = unos.trim().split("\\s+");   // splitujemo po space

        if (parts.length != 2)                              // ako ima vise od 2 dela, vrati false i reci neispravan unos
            return true;

        // samo provera da li prvi karakter pocinje sa -,+,~
        boolean firstPartValid = parts[0].length() > 0 &&
                (parts[0].charAt(0) == '+' ||
                        parts[0].charAt(0) == '-' ||
                        parts[0].charAt(0) == '#' ||
                        parts[0].charAt(0) == '~');

        boolean secondPartValid = parts[1].trim().equals("int") || parts[1].trim().equals("String") || parts[1].trim().equals("boolean") || parts[1].trim().equals("double") || proveriDaLiImaNekaInstancaOvogTipa(parts[1].trim());

        return !firstPartValid || !secondPartValid;
    }

    public boolean proveriNetacnostUnosaZaMetodu(String unos){
        if(unos.equalsIgnoreCase(""))
            return false;

        String[] parts = unos.trim().split("\\s+");   // splitujemo po space

        if (parts.length != 3)                               // ako ima vise od 2 dela, vrati false i reci neispravan unos
            return true;

        // samo provera da li prvi karakter pocinje sa -,+,~
        boolean firstPartValid = parts[0].length() > 0 &&
                (parts[0].charAt(0) == '+' ||
                        parts[0].charAt(0) == '-' ||
                        parts[0].charAt(0) == '#' ||
                        parts[0].charAt(0) == '~');

        boolean secondPartValid = true;
        String uZagradama = parts[1];
        uZagradama = uZagradama.substring(1, uZagradama.length() - 2);      // od (boolean,int,int): skinuli smo zagrade i :
        String[] contentUZagradama = uZagradama.split(",");
        for(String s : contentUZagradama){
            if (!s.equals("int") && !s.equals("boolean") && !s.equals("double") && !s.equals("String") && !proveriDaLiImaNekaInstancaOvogTipa(s)) {
                secondPartValid = false;
                System.out.println("ako vrati false, s je:");
                System.out.println(s);
                break;
            }
        }

        boolean thirdPartValid = parts[2].trim().equals("int") || parts[2].trim().equals("String") || parts[2].trim().equals("boolean") || parts[2].trim().equals("double") || proveriDaLiImaNekaInstancaOvogTipa(parts[2].trim());

        return !firstPartValid || !secondPartValid || !thirdPartValid;
    }

    public void dodajHintText(String hintText, JTextArea textArea) {
        textArea.setText(hintText);
        textArea.setForeground(java.awt.Color.GRAY);

        textArea.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textArea.getText().equals(hintText)) {
                    textArea.setText("");
                    textArea.setForeground(java.awt.Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textArea.getText().isEmpty()) {
                    textArea.setText("");
                    textArea.setForeground(java.awt.Color.GRAY);
                }
            }
        });
    }

    // BIG BRAIN TIME: moze kao tip takodje biti i instanca neke klase, NE SMEMO OVO DA ZABORAVIMO!
    private boolean proveriDaLiImaNekaInstancaOvogTipa(String tip){
        for(ElementPainter p : painters){
            if(p instanceof InterclassPainter){
                DiagramElement diagramElement = ((InterclassPainter) p).getDiagramElement();
                if(diagramElement instanceof Interclass){
                    String imePrethodnoUneseneKlase = diagramElement.getName();
                    if(tip.equalsIgnoreCase(imePrethodnoUneseneKlase)){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public ClassyTreeItem findTheItem(ClassyTreeItem item, ClassyNode diagram) {
        if (item.getClassyNode().equals(diagram))
            return item;            // ako je to taj, daj mi ga da na njega stavim diagramElement
        for (int i = 0; i < item.getChildCount(); i++) {
            ClassyTreeItem classyItemNext = findTheItem((ClassyTreeItem) item.getChildAt(i), diagram);
            if (classyItemNext != null)
                return classyItemNext;
        }
        return null;
    }
    public void resetCommandStack(){
        commandManager.resetStack();
    }

    public String getTabName() {
        return tabName;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public List<ElementPainter> getPainters() {
        return painters;
    }

    public void setPainters(List<ElementPainter> painters) {
        this.painters = painters;
    }

    public Diagram getDiagram() {
        return diagram;
    }

    public void setDiagram(Diagram diagram) {
        this.diagram = diagram;
    }

    public List<ElementPainter> getSelectionedRectangles() {
        return selectionedRectangles;
    }

    public void setSelectionedRectangles(List<ElementPainter> selectionedRectangles) {
        this.selectionedRectangles = selectionedRectangles;
    }

    public Rectangle getSelectionRectangle() {
        return selectionRectangle;
    }

    public void setSelectionRectangle(Rectangle selectionRectangle) {
        this.selectionRectangle = selectionRectangle;
    }

    public Line2D getTemporaryLine() {
        return temporaryLine;
    }

    public void setTemporaryLine(Line2D temporaryLine) {
        this.temporaryLine = temporaryLine;
    }
    public void addPainter(ElementPainter painter) {
        painters.add(painter);
    }
    public void removePainter(ElementPainter painter){
        painters.remove(painter);
    }

    public void addRectangleToSelected(ElementPainter elementPainter){
        selectionedRectangles.add(elementPainter);
    }

    public void removeRectangleFromSelected(ElementPainter elementPainter){
        selectionedRectangles.remove(elementPainter);
    }

    public void addConnectionsToSelected(ElementPainter elementPainter){
        selectionedConnections.add(elementPainter);
    }

    public void removeConnectionsFromSelected(ElementPainter elementPainter){
        selectionedConnections.add(elementPainter);
    }

    public List<ElementPainter> getSelectionedConnections() {
        return selectionedConnections;
    }

    public void setSelectionedConnections(List<ElementPainter> selectionedConnections) {
        this.selectionedConnections = selectionedConnections;
    }

    public AffineTransform getTransform() {
        return transform;
    }

    public void setTransform(AffineTransform transform) {
        this.transform = transform;
    }
}


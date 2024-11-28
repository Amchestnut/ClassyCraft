package raf.dsw.classycraft.app.gui.swing.view;

import raf.dsw.classycraft.app.controller.commandActions.CommandManager;
import raf.dsw.classycraft.app.controller.mouseAdapters.MouseAdapter;
import raf.dsw.classycraft.app.gui.swing.painters.ElementPainter;
import raf.dsw.classycraft.app.gui.swing.painters.element_painters.*;
import raf.dsw.classycraft.app.gui.swing.painters.connection_painters.ConnectionPainter;
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
    private List<ElementPainter> selectionedRectangles;  // list of painters, because painter has the model, but model doesnt have the painter ;) (MVC)
    private Diagram diagram;
    private Rectangle selectionRectangle = null;
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
                            iterator.remove(); // Using iterator to remove the element
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
            float[] dash1 = {10.0f};            // 10 pixel dash
            BasicStroke dashed =
                    new BasicStroke(1.0f,
                            BasicStroke.CAP_BUTT,
                            BasicStroke.JOIN_MITER,
                            10.0f, dash1, 0.0f); // Here I create the stroke, and give him this properties

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

        DataForElementFromDialog dataForElementFromDialog = new DataForElementFromDialog();   // OBJECT where I save the typed data

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

        // text area for attributes and methods
        JTextArea attributesArea = new JTextArea(5, 20);
        attributesArea.setBorder(BorderFactory.createTitledBorder("Attributes"));
        dodajHintText("-Enter attributes in the following format: \n" +
                        "-visibility, attribute name, return type, for example: \n" +
                        "-numberOfPages: int                         or:\n" +
                        "+bookTitle: String                         or:\n" +
                        "#pageThickness: double                     or:\n" +
                        "~isBookRead: boolean"
                , attributesArea);


        JTextArea methodsArea = new JTextArea(5, 20);
        methodsArea.setBorder(BorderFactory.createTitledBorder("Methods"));
        dodajHintText("-Enter methods in the following format: \n" +
                        "-methodName (argument types, e.g., int or String,int,int): return type, for example: \n" +
                        "+calculateNumberOfLettersOnPage (int): int            or:\n" +
                        "#whatDayIsItToday: (String): String                   or:\n" +
                        "~hasItBeenRead: (int): boolean"
                , methodsArea);


        // Cant add attributes in interface, and methods in enum
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
        dialog.pack();  // size this dialog
        dialog.setLocationRelativeTo(null);  // center
        final boolean[] addButtonPressed = {false};


        // when i click ok, collect all data
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
            if (attributesArea.getText().equalsIgnoreCase("-Enter attributes in the following format: \n" +
                    "-visibility, attribute name, return type, for example: \n" +
                    "-numberOfPages: int                         or:\n" +
                    "+bookTitle: String                         or:\n" +
                    "#pageThickness: double                     or:\n" +
                    "~isBookRead: boolean")) {
                attributesArea.setText("");
            }
            if (methodsArea.getText().equalsIgnoreCase("-Enter methods in the following format: \n" +
                    "-methodName (argument types, e.g., int or String,int,int): return type, for example: \n" +
                    "+calculateNumberOfLettersOnPage (int): int            or:\n" +
                    "#whatDayIsItToday: (String): String                   or:\n" +
                    "~hasItBeenRead: (int): boolean")) {
                methodsArea.setText("");
            }

            String[] allAtributes = attributesArea.getText().split("\n");
            boolean attributesOK = true;
            // Check for all entered attributes, except if it's an interface
            if(!interfaceButton.isSelected()){
                for(String atribut: allAtributes){
                    if(proveriNetacnostUnosaZaAtribut(atribut)){  // If returns false, attributes are not ok
                        attributesOK = false;
                    }
                }
            }

            String[] sveMetode = methodsArea.getText().split("\n");
            boolean metodeOK = true;
            // checking for all entered methods, except if it's an Enum
            if(!enumButton.isSelected()){
                for(String metoda: sveMetode){
                    if(proveriNetacnostUnosaZaMetodu(metoda)){
                        metodeOK = false;
                    }
                }
            }

            String visibilityOfTheClass = nameField.getText();
            boolean checkIfEnteredVisibility = visibilityOfTheClass.isEmpty() || visibilityOfTheClass.charAt(0) == '+' || visibilityOfTheClass.charAt(0) == '-' || visibilityOfTheClass.charAt(0) == '#' || visibilityOfTheClass.charAt(0) == '~';
            if(!checkIfEnteredVisibility){
                JOptionPane.showMessageDialog(dialog, "Class visibility not correctly entered!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!attributesOK) {
                JOptionPane.showMessageDialog(dialog, "Attribute not correctly entered", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!metodeOK) {
                JOptionPane.showMessageDialog(dialog, "Method not correctly entered", "Input Error", JOptionPane.ERROR_MESSAGE);
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
        attributesArea.setEnabled(!typeOfTheSelectedInterclassWas.equalsIgnoreCase("Interface"));   // Can't edit attributes textfield in interface
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

                String visibilityOfTheClass = nameField.getText();
                boolean checkIfEnteredVisibility = visibilityOfTheClass.isEmpty() || visibilityOfTheClass.charAt(0) == '+' || visibilityOfTheClass.charAt(0) == '-' || visibilityOfTheClass.charAt(0) == '#' || visibilityOfTheClass.charAt(0) == '~';

                if (!checkIfEnteredVisibility) {
                    JOptionPane.showMessageDialog(dialog, "The visibility of the class is not entered correctly", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // !enumButton.isSelected() &&
                if (attributesArea.getText().equalsIgnoreCase("-Enter attributes in the following format: \n" +
                        "-visibility, attribute name, return type, for example: \n" +
                        "-numberOfPages: int                         or:\n" +
                        "+bookTitle: String                         or:\n" +
                        "#pageThickness: double                     or:\n" +
                        "~isBookRead: boolean")) {
                    attributesArea.setText("");
                }
                if (methodsArea.getText().equalsIgnoreCase("-Enter methods in the following format: \n" +
                        "-methodName (argument types, e.g., int or String,int,int): return type, for example: \n" +
                        "+calculateNumberOfLettersOnPage (int): int            or:\n" +
                        "#whatDayIsItToday: (String): String                   or:\n" +
                        "~hasItBeenRead: (int): boolean")) {
                    methodsArea.setText("");
                }

                String[] allAtributes = attributesArea.getText().split("\n");
                boolean attributesOK = true;
                // check for all entered attributes, except if it's an interface
                if(!interfaceButton.isSelected()){
                    for(String atribut: allAtributes){
                        if(proveriNetacnostUnosaZaAtribut(atribut)){
                            attributesOK = false;
                        }
                    }
                }

                String[] sveMetode = methodsArea.getText().split("\n");
                boolean metodeOK = true;
                // check all methods except if it is an Enum
                if(!enumButton.isSelected()){
                    for(String metoda: sveMetode){
                        if(proveriNetacnostUnosaZaMetodu(metoda)){
                            metodeOK = false;
                        }
                    }
                }

                if (!attributesOK) {
                    JOptionPane.showMessageDialog(dialog, "Attribute not correctly entered", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!metodeOK) {
                    JOptionPane.showMessageDialog(dialog, "Method not correctly entered", "Input Error", JOptionPane.ERROR_MESSAGE);
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

        JRadioButton associationButton = new JRadioButton("Association connection");
        JRadioButton inheritanceButton = new JRadioButton("Inheritance connection");
        JRadioButton realisationButton = new JRadioButton("Realisation connection");
        JRadioButton dependencyButton = new JRadioButton("Dependency connection");
        JRadioButton aggregationButton = new JRadioButton("Aggregation connection");
        JRadioButton compositionButton = new JRadioButton("Composition connection", true);   // default true

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

        JLabel labelFrom = new JLabel("The element FROM which the connection originates has an instance of the TO element in the form of:"); // label
        JTextArea textAreaFrom = new JTextArea(1, 20); // single-line text area
        textAreaFrom.setBorder(BorderFactory.createTitledBorder("Instance of interclass FROM"));
        dodajHintText("-literatureType 0..1", textAreaFrom); // hint text
        JScrollPane firstScrollPane = new JScrollPane(textAreaFrom);

        JLabel labelTo = new JLabel("The element TO, where the connection ends, has an instance of the FROM element in the form of:");
        JTextArea textAreaTo = new JTextArea(1, 20);
        textAreaTo.setBorder(BorderFactory.createTitledBorder("Instance of interclass TO"));
        dodajHintText("-books 0..*", textAreaTo);
        JScrollPane secondScrollPane = new JScrollPane(textAreaTo);

        dialog.add(labelFrom);
        dialog.add(firstScrollPane);
        dialog.add(labelTo);
        dialog.add(secondScrollPane);

        // Listener to enable/disable TextArea based on what button is clicked
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

            boolean firstIsEmpty = false;
            boolean secondIsEmpty = false;

            if(enteredText1.equalsIgnoreCase("")){
                firstText = true;
                firstIsEmpty = true;
            }
            if(enteredText2.equalsIgnoreCase("")){
                secondText = true;
                secondIsEmpty = true;
            }
            boolean bothAreEmpty = false;

            if (associationButton.isSelected() || aggregationButton.isSelected() || compositionButton.isSelected()) {
                bothAreEmpty = firstIsEmpty && secondIsEmpty; // Both cannot be empty, unless it's another type of connection

                if (enteredText1.equalsIgnoreCase("-literatureType 0..1") || enteredText2.equalsIgnoreCase("-books 0..*")) {
                    JOptionPane.showMessageDialog(dialog, "Default text cannot remain", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if (associationButton.isSelected() || aggregationButton.isSelected() || compositionButton.isSelected()) {
                boolean result = firstText && secondText && (!bothAreEmpty);
                if (!result) {
                    JOptionPane.showMessageDialog(dialog, "Invalid input! Both fields cannot be empty, or some input is incorrect", "Input Error", JOptionPane.ERROR_MESSAGE);
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
            return null;   // dialog closed but OK wasn't clicked
        }

        // Extract selected values
        String connectionType = null;

        if (associationButton.isSelected()) {
            connectionType = "association";
        }
        else if (inheritanceButton.isSelected()) {
            connectionType = "inheritance";
        }
        else if (realisationButton.isSelected()) {
            connectionType = "realisation";
        }
        else if (dependencyButton.isSelected()) {
            connectionType = "dependency";
        }
        else if (aggregationButton.isSelected()) {
            connectionType = "aggregation";
        }
        else if (compositionButton.isSelected()) {
            connectionType = "composition";
        }

        DataForConnection dataForConnection = new DataForConnection(connectionType);

        // If association, aggregation or composition connection selected, add him other parameters except the type
        if(associationButton.isSelected() || aggregationButton.isSelected() || compositionButton.isSelected()){
            String firstText = textAreaFrom.getText();
            String secondText = textAreaTo.getText();

            if(!firstText.equalsIgnoreCase("")){                  // if not empty
                String[] parts1 = firstText.trim().split("\\s+");
                String visibilityFrom = parts1[0].substring(0, 1);           // First char (visibility)
                String instanceFrom = parts1[0].substring(1);      // instance

                dataForConnection.setVisibilityOfTheFirstElement(visibilityFrom);  // Nicely created custom Data Object that accept this
                dataForConnection.setInstanceOfTheFirstElement(instanceFrom);
                dataForConnection.setCardinalityOfTheFirstElement(parts1[1]);
            }

            if(!secondText.equalsIgnoreCase("")){
                String[] parts2 = secondText.trim().split("\\s+");
                String visibilityTo = parts2[0].substring(0, 1);
                String instanceTo = parts2[0].substring(1);

                dataForConnection.setVisibilityOfTheSecondElement(visibilityTo);
                dataForConnection.setInstanceOfTheSecondElement(instanceTo);
                dataForConnection.setCardinalityOfTheSecondElement(parts2[1]);
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

        JRadioButton associationButton = new JRadioButton("Association Connection");
        JRadioButton inheritanceButton = new JRadioButton("Inheritance Connection");
        JRadioButton realisationButton = new JRadioButton("Realisation Connection");
        JRadioButton dependencyButton = new JRadioButton("Dependency Connection");
        JRadioButton aggregationButton = new JRadioButton("Aggregation Connection");
        JRadioButton compositionButton = new JRadioButton("Composition Connection");

        String type = preFilledData.getType();
        if (type.equalsIgnoreCase("association")) {
            associationButton.setSelected(true);
        } else if (type.equalsIgnoreCase("inheritance")) {
            inheritanceButton.setSelected(true);
        } else if (type.equalsIgnoreCase("realisation")) {
            realisationButton.setSelected(true);
        } else if (type.equalsIgnoreCase("dependency")) {
            dependencyButton.setSelected(true);
        } else if (type.equalsIgnoreCase("aggregation")) {
            aggregationButton.setSelected(true);
        } else if (type.equalsIgnoreCase("composition")) {
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


        String inputFromFirstTextField = "";
        inputFromFirstTextField += preFilledData.getVisibilityOfTheFirstElement() +
                preFilledData.getInstanceOfTheFirstElement() + " " +
                preFilledData.getCardinalityOfTheFirstElement();

        String inputFromSecondTextField = "";
        inputFromSecondTextField = preFilledData.getVisibilityOfTheSecondElement() +
                preFilledData.getInstanceOfTheSecondElement() + " " +
                preFilledData.getCardinalityOfTheSecondElement();

        JLabel labelFrom = new JLabel("The element FROM which the connection originates has an instance of the TO element in the form:");
        JTextArea textAreaFrom = new JTextArea(1, 20);
        textAreaFrom.setBorder(BorderFactory.createTitledBorder("Instance of Interclass FROM"));
        textAreaFrom.setText(inputFromFirstTextField);
        JScrollPane firstScrollPane = new JScrollPane(textAreaFrom);

        JLabel labelTo = new JLabel("The element TO where the connection ends has an instance of the FROM element in the form:");
        JTextArea textAreaTo = new JTextArea(1, 20);
        textAreaTo.setBorder(BorderFactory.createTitledBorder("Instance of Interclass TO"));
        textAreaTo.setText(inputFromSecondTextField);
        JScrollPane secondScrollPane = new JScrollPane(textAreaTo);

        dialog.add(labelFrom);
        dialog.add(firstScrollPane);
        dialog.add(labelTo);
        dialog.add(secondScrollPane);

        if(type.equalsIgnoreCase("inheritance") || type.equalsIgnoreCase("realisation") || type.equalsIgnoreCase("dependency")){
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

            boolean firstIsEmpty = false;
            boolean secondIsEmpty = false;

            if(enteredText1.equalsIgnoreCase("")){
                firstText = true;
                firstIsEmpty = true;
            }
            if(enteredText2.equalsIgnoreCase("")){
                secondText = true;
                secondIsEmpty = true;
            }
            boolean bothAreEmpty = false;
            if(associationButton.isSelected() || aggregationButton.isSelected() || compositionButton.isSelected()){
                bothAreEmpty = firstIsEmpty && secondIsEmpty;

                if (enteredText1.equalsIgnoreCase("-literaryType 0..1") || enteredText2.equalsIgnoreCase("-books 0..*")) {
                    JOptionPane.showMessageDialog(dialog, "Default text cannot remain", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if(associationButton.isSelected() || aggregationButton.isSelected() || compositionButton.isSelected()){
                boolean result = firstText && secondText && (!bothAreEmpty);
                if(!result){
                    JOptionPane.showMessageDialog(dialog, "Both can't be empty, or entered instance is not typed correctly!", "Input Error", JOptionPane.ERROR_MESSAGE);
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
            return null;
        }

        // Extract selected values. Also, this may be duplicated
        String connectionType = null;

        if (associationButton.isSelected()) {
            connectionType = "association";
        }
        else if (inheritanceButton.isSelected()) {
            connectionType = "inheritance";
        }
        else if (realisationButton.isSelected()) {
            connectionType = "realisation";
        }
        else if (dependencyButton.isSelected()) {
            connectionType = "dependency";
        }
        else if (aggregationButton.isSelected()) {
            connectionType = "aggregation";
        }
        else if (compositionButton.isSelected()) {
            connectionType = "composition";
        }

        DataForConnection dataForConnection = new DataForConnection(connectionType);

        if(associationButton.isSelected() || aggregationButton.isSelected() || compositionButton.isSelected()){
            String firstText = textAreaFrom.getText();
            String secondText = textAreaTo.getText();

            if(!firstText.equalsIgnoreCase("")){
                String[] parts1 = firstText.trim().split("\\s+");
                String visibilityFrom = parts1[0].substring(0, 1);
                String instanceFrom = parts1[0].substring(1);

                dataForConnection.setVisibilityOfTheFirstElement(visibilityFrom);
                dataForConnection.setInstanceOfTheFirstElement(instanceFrom);
                dataForConnection.setCardinalityOfTheFirstElement(parts1[1]);
            }

            if(!secondText.equalsIgnoreCase("")){
                String[] parts2 = secondText.trim().split("\\s+");
                String visibilityTo = parts2[0].substring(0, 1);
                String instanceTo = parts2[0].substring(1);

                dataForConnection.setVisibilityOfTheSecondElement(visibilityTo);
                dataForConnection.setInstanceOfTheSecondElement(instanceTo);
                dataForConnection.setCardinalityOfTheSecondElement(parts2[1]);
            }

        }

        return dataForConnection;
    }

    public void backToOriginalColor(){

        for(ElementPainter painter : painters){
            if(painter instanceof KlasaPainter){
                DiagramElement diagramElement = ((KlasaPainter)painter).getDiagramElement();
                diagramElement.setColor(0xFFFF4040);
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

        String[] parts = unos.trim().split("\\s+");   // split by space

        if (parts.length != 2)
            return false;

        // visibility check -,+,~
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

        String[] parts = unos.trim().split("\\s+");

        if (parts.length != 2)
            return true;

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

        String[] parts = unos.trim().split("\\s+");

        if (parts.length != 3)
            return true;

        boolean firstPartValid = parts[0].length() > 0 &&
                (parts[0].charAt(0) == '+' ||
                        parts[0].charAt(0) == '-' ||
                        parts[0].charAt(0) == '#' ||
                        parts[0].charAt(0) == '~');

        boolean secondPartValid = true;
        String uZagradama = parts[1];
        uZagradama = uZagradama.substring(1, uZagradama.length() - 2);      // from (boolean,int,int): we take off the brackets
        String[] contentUZagradama = uZagradama.split(",");
        for(String s : contentUZagradama){
            if (!s.equals("int") && !s.equals("boolean") && !s.equals("double") && !s.equals("String") && !proveriDaLiImaNekaInstancaOvogTipa(s)) {
                secondPartValid = false;
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

    // BIG BRAIN TIME: type can also be an instance of some class, cant forget this !!!
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
            return item;            // If it is the one, give it to me so i can put diagramElement in him
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


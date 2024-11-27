package raf.dsw.classycraft.app.gui.swing.view;

import javax.swing.*;

public class UserGuideFrame extends JFrame {
    public UserGuideFrame(){
        this.setTitle("User Guide");
        this.setSize(1000, 600);
        this.setLocationRelativeTo(null);

        JTextArea guideText = new JTextArea(10, 40);
        guideText.setText("GUIDE FOR CREATING AN INTERCLASS: \n" +
                "The name of the interclass must start with +,-,#,~ to define its visibility. It can also be left blank. \n" +
                "Classes are red, Abstract classes are yellow, Interfaces are green, and Enums are blue.\n" +
                "The TYPE can be: int, boolean, String, double, OR THE NAME OF ANY EXISTING CLASS WE PREVIOUSLY ADDED (this is also an option).\n" +
                "Attributes are in the format: visibility + attribute name: + type \n" +
                "TEST EXAMPLE: -numberOfPages: int \n" +
                "Methods are in the format: visibility + method name + (any argument types, separated by commas, e.g., int,double,String) + return type\n" +
                "TEST EXAMPLE: #lettersCountInName (String,double): int  \n\n\n" +

                "GUIDE FOR CREATING A CONNECTION: \n" +
                "For INHERITANCE, REALIZATION, DEPENDENCY connections, you can add a connection without any specific rules.\n" +
                "For ASSOCIATION, AGGREGATION, or COMPOSITION connections, follow these rules:\n" +
                "   a) You cannot leave the default hint text in the text area; at least one click is required to remove it.\n" +
                "   b) The cardinality description of the connection must be in the format:\n" +
                "           visibility + instance name + cardinality, e.g., -----> -literaryType 0..1\n" +
                "   c) One text field can remain empty (per UML rules), but at least one click is required to remove the HINT text.\n\n\n" +

                "GUIDE FOR USING STATES \n " +
                "1. Select - Adds or removes elements from the LIST of selected interclasses/connections (located in diagramView). " +
                "Whenever the mouse is dragged by even a millimeter, it checks if any element is currently selected.\n" +
                "The logic changes the color of the selected interclass to white, and connections to orange (via setColor(), " +
                "which triggers a notify in the model). If deselected, the color reverts to its original via observer and setColor().\n\n" +

                "2. Move - To use move, elements must first be SELECTED. \n" +
                "Then, click and drag any selected interclass (connections cannot be moved) to reposition it.\n\n" +

                "3. Add interclass - Adds a user-specified interclass type to the diagramView. \n" +
                "If any element was previously selected, it will be deselected via the 'backToOriginalColor()' method in diagramView.\n\n" +

                "4. Add Connection - Adds a connection between two interclasses. Fields FROM and TO are added in the model. " +
                "Connections are deleted similarly.\n" +
                "To successfully draw a connection, first click on one class and drag to another class.\n" +
                "If the mouse click is not released on the second class, the connection will not be drawn.\n\n" +

                "5. Edit - To edit an interclass/connection, selection is not required. Just click on it to edit.\n" +
                "After clicking SAVE, all changes are stored in the MODEL. If anything changes, notify is called.\n\n" +

                "6. Delete - To delete an interclass/connection, it MUST first be selected! \n" +
                "This is implemented to allow multi-deletion when multiple elements are selected.\n" +
                "To successfully delete, click on any selected element (interclass/connection).\n" +
                "PAY ATTENTION TO CONNECTION DELETION!!!\n" +
                "Connections create a bounding rectangle for selection, but if the connection is very HORIZONTAL, " +
                "the rectangle might be very thin, making it difficult to click. Ensure precise clicks.\n" +
                "For testing, uncomment the code that draws a pink rectangle in CONNECTIONPAINTER.\n\n" +

                "7. Zoom - For zooming, hold the mouse click and scroll the wheel! \n" +
                "Scrolling without holding the click will not work.\n\n" +

                "8. Zoom to fit - Add a class before clicking this button to capture its top and left coordinates.\n\n" +

                "ADDING TO TREE VIEW: \n" +
                "Adding DIAGRAM ELEMENTS to the left-side tree view (TREEVIEW) works successfully, " +
                "but you need to double-click the diagram to expand its area.\n");

        guideText.setWrapStyleWord(true);
        guideText.setLineWrap(true);
        guideText.setCaretPosition(0);
        guideText.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(guideText);
        this.add(scrollPane);
        this.setVisible(true);
    }
}
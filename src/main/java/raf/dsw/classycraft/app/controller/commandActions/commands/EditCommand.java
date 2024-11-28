package raf.dsw.classycraft.app.controller.commandActions.commands;

import raf.dsw.classycraft.app.controller.commandActions.AbstractCommand;
import raf.dsw.classycraft.app.gui.swing.tree.ClassyTreeImplementation;
import raf.dsw.classycraft.app.gui.swing.tree.model.ClassyTreeItem;
import raf.dsw.classycraft.app.gui.swing.view.DiagramView;
import raf.dsw.classycraft.app.gui.swing.view.MainFrame;
import raf.dsw.classycraft.app.model.diagramElements.DiagramElement;
import raf.dsw.classycraft.app.model.diagramElements.connections.Connection;
import raf.dsw.classycraft.app.model.diagramElements.connections.DataForConnection;
import raf.dsw.classycraft.app.model.diagramElements.elements.Enum;
import raf.dsw.classycraft.app.model.diagramElements.elements.*;
import raf.dsw.classycraft.app.model.state.classesAndConnections.Company;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static raf.dsw.classycraft.app.model.diagramElements.elements.Constants.CHARACTER_WIDTH;

public class EditCommand extends AbstractCommand {

    private DiagramView diagramView;
    private DiagramElement diagramElement;
    private String oldType;
    private String newType;
    private String oldName;
    private String newName;
    private List<ClassContent> oldMethods;
    private List<ClassContent> newMethods;
    private List<ClassContent> oldAtributes;
    private List<ClassContent> newAtributes;
    private Company company;
    private boolean isDone;
    private boolean nothingHasBeenDone = true;
    public EditCommand(DiagramView diagramView, DiagramElement diagramElement){
        company = new Company();
        this.diagramView = diagramView;
        this.diagramElement = diagramElement;
        isDone = false;
    }
    @Override
    public void doCommand() {
        if(!isDone) {
            // u ovom objektu ispod imam sve podatke iz prosledjenog diagram elementa !
            if (diagramElement instanceof Interclass) {
                DataForElementFromDialog dataForElementFromDialog = extractDataFromInterclass(diagramElement);

                DataForElementFromDialog updejtovanData = null;
                if (dataForElementFromDialog != null) {
                    oldName = dataForElementFromDialog.getName();
                    oldType = dataForElementFromDialog.getType();
                    updejtovanData = diagramView.showEditDialogForInterclass(dataForElementFromDialog);
                    if(updejtovanData == null)
                        return;
                    newType = updejtovanData.getType();
                }

                if (updejtovanData != null) {
                    nothingHasBeenDone = false;
                    String[] rawAttributes = company.getRawAttributesFromDataFromElementFromDialog(dataForElementFromDialog);
                    String[] rawMethods = company.getRawMethodsFromDataFromElementFromDialog(dataForElementFromDialog);

                    List<ClassContent> atributi = new ArrayList<>();
                    company.refactorAttributes(rawAttributes, atributi);

                    List<ClassContent> metode = new ArrayList<>();
                    company.refactorMethods(rawMethods, metode);

                    String name = updejtovanData.getName();
                    if (name.isEmpty()) {
                        name = "+EmptyName";
                    }
                    newName = name;
                    diagramElement.setName(name);
                    setMethodsAndAttributesForDiagramElement(diagramElement, metode, atributi);
                    ((Interclass) diagramElement).setDimension(company.determiningTheSizeOfTheRectangle(name, atributi, metode));
                }
                else{
                    nothingHasBeenDone = true;
                }
            } else if (diagramElement instanceof Connection) {
                DataForConnection dataForConnection = extractDataFromConnection(diagramElement);

                DataForConnection updatedData = null;
                if (dataForConnection != null) {
                    updatedData = diagramView.showEditDialogForConnections(dataForConnection);
                }
                if (updatedData != null) {
                    nothingHasBeenDone = false;
                    if (updatedData.getType().equalsIgnoreCase("asocijacija") || updatedData.getType().equalsIgnoreCase("agregacija") || updatedData.getType().equalsIgnoreCase("kompozicija")) {

                        String visibilityOfTheFirstElement = updatedData.getVisibilityOfTheFirstElement();
                        String instanceOfTheFirstElement = updatedData.getInstanceOfTheFirstElement();
                        String kardinalnostOfTheFirstElement = updatedData.getCardinalityOfTheFirstElement();

                        String visibilityOfTheSecondElement = updatedData.getVisibilityOfTheSecondElement();
                        String instanceOfTheSecondElement = updatedData.getInstanceOfTheSecondElement();
                        String kardinalnostOfTheSecondElement = updatedData.getCardinalityOfTheSecondElement();

                        ((Connection) diagramElement).setVisibilityOfTheFirstElement(visibilityOfTheFirstElement);
                        ((Connection) diagramElement).setInstanceOfTheFirstElement(instanceOfTheFirstElement);
                        ((Connection) diagramElement).setCardinalityOfTheFirstElement(kardinalnostOfTheFirstElement);

                        ((Connection) diagramElement).setVisibilityOfTheSecondElement(visibilityOfTheSecondElement);
                        ((Connection) diagramElement).setInstanceOfTheSecondElement(instanceOfTheSecondElement);
                        ((Connection) diagramElement).setCardinalityOfTheSecondElement(kardinalnostOfTheSecondElement);
                    }
                }
                else{
                    nothingHasBeenDone = true;
                }
            }
            if (diagramElement instanceof Interclass || diagramElement instanceof Connection) {
                ClassyTreeImplementation classyTree = (ClassyTreeImplementation) MainFrame.getInstance().getClassyTree();
                ClassyTreeItem root = classyTree.getRoot();
                ClassyTreeItem diagramElementNode = diagramView.findTheItem(root, diagramElement);

                if (diagramElementNode != null) {
                    diagramElementNode.setUserObject(diagramElement);                  // updejtovan node
                    SwingUtilities.updateComponentTreeUI(classyTree.getTreeView());    // refresh
                }
            }
            isDone = true;
        }
        else{
            turnOldToNewMethodsAndAttributes(newName, newAtributes, newMethods);
        }
        setChangedToTrueInCurrentProject();
    }

    @Override
    public void undoCommand() {
        turnOldToNewMethodsAndAttributes(oldName, oldAtributes, oldMethods);
    }

    private void turnOldToNewMethodsAndAttributes(String oldName, List<ClassContent> oldAtributes, List<ClassContent> oldMethods) {
        diagramElement.setName(oldName);
        if(diagramElement instanceof Klasa){
            ((Klasa) diagramElement).setAttributes(oldAtributes);
            ((Klasa) diagramElement).setMethods(oldMethods);
        }
        else if(diagramElement instanceof Interfejs){
            // nema atribute
            ((Interfejs) diagramElement).setMethods(oldMethods);
        }
        else if(diagramElement instanceof Enum){
            ((Enum) diagramElement).setAttributes(oldAtributes);
            ((Enum) diagramElement).setMethods(oldMethods);
        }
        else if(diagramElement instanceof AbstractClass){
            ((AbstractClass) diagramElement).setAttributes(oldAtributes);
            ((AbstractClass) diagramElement).setMethods(oldMethods);
        }
        ((Interclass) diagramElement).setDimension(company.determiningTheSizeOfTheRectangle(oldName, oldAtributes, oldMethods));
    }

    private void setMethodsAndAttributesForDiagramElement(DiagramElement diagramElement, List<ClassContent> metode, List<ClassContent> atributi){
        if (diagramElement instanceof Klasa) {
            oldMethods = ((Klasa) diagramElement).getMethods();
            oldAtributes = ((Klasa) diagramElement).getAttributes();
            newMethods = metode;
            newAtributes = atributi;
            ((Klasa) diagramElement).setAttributes(newAtributes);
            ((Klasa) diagramElement).setMethods(newMethods);
        } else if (diagramElement instanceof Interfejs) {
            oldMethods = ((Interfejs) diagramElement).getMethods();
            newMethods = metode;
            // nema atribute
            ((Interfejs) diagramElement).setMethods(newMethods);
        } else if (diagramElement instanceof Enum) {
            oldMethods = ((Enum) diagramElement).getMethods();
            oldAtributes = ((Enum) diagramElement).getAttributes();
            newMethods = metode;
            newAtributes = atributi;
            ((Enum) diagramElement).setAttributes(newAtributes);
            ((Enum) diagramElement).setMethods(newMethods);
        } else if (diagramElement instanceof AbstractClass) {
            oldAtributes = ((AbstractClass) diagramElement).getAttributes();
            oldMethods = ((AbstractClass) diagramElement).getMethods();
            newMethods = metode;
            newAtributes = atributi;
            ((AbstractClass) diagramElement).setAttributes(newAtributes);
            ((AbstractClass) diagramElement).setMethods(newMethods);
        }
    }

    public DataForElementFromDialog extractDataFromInterclass(DiagramElement diagramElement) {
        DataForElementFromDialog data = new DataForElementFromDialog();

        String name = diagramElement.getName();

        List<ClassContent> attributes = new ArrayList<>();
        List<ClassContent> methods = new ArrayList<>();

        if(diagramElement instanceof Klasa){
            attributes.addAll(((Klasa)diagramElement).getAttributes());
            methods.addAll(((Klasa)diagramElement).getMethods());
        }
        else if(diagramElement instanceof Interfejs){
            methods.addAll(((Interfejs)diagramElement).getMethods());
        }
        else if(diagramElement instanceof Enum){
            attributes.addAll(((Enum)diagramElement).getAttributes());
            methods.addAll(((Enum)diagramElement).getMethods());
        }
        else if(diagramElement instanceof AbstractClass){
            attributes.addAll(((AbstractClass)diagramElement).getAttributes());
            methods.addAll(((AbstractClass)diagramElement).getMethods());
        }

        // convertujem attributes i methods u string format
        StringBuilder attributesString = new StringBuilder();
        for (ClassContent attribute : attributes) {
            if(attribute instanceof Attribute){
                attributesString.append(attribute.getVidljivost()).append(((Attribute) attribute).getAttributeName()).append("\n");
            }
        }

        StringBuilder methodsString = new StringBuilder();
        for (ClassContent method : methods) {
            if(method instanceof Method){
                methodsString.append(method.getVidljivost()).append(((Method) method).getMethodName()).append("\n");
            }
        }

        data.setName(name);
        if(diagramElement instanceof Klasa){
            data.setType("Class");
        }
        else if(diagramElement instanceof Interfejs){
            data.setType("Interface");
        }
        else if(diagramElement instanceof Enum){
            data.setType("Enum Class");
        }
        else if(diagramElement instanceof AbstractClass){
            data.setType("Abstract Class");
        }
        data.setAttributes(attributesString.toString());
        data.setMethods(methodsString.toString());

        return data;
    }
    public DataForConnection extractDataFromConnection(DiagramElement diagramElement) {
        if (!(diagramElement instanceof Connection)) {                                           // lakse se kastuje posle ovog
            return null;
        }

        Connection connection = (Connection) diagramElement;
        String type = connection.getType();

        DataForConnection dataForConnection = new DataForConnection(type);

        // ako je veza imala instance (ako je bila asocijacija ili neka druga koja ima), popunim data object sa tim podacima i vratim
        if(type.equalsIgnoreCase("asocijacija") || type.equalsIgnoreCase("agregacija") || type.equalsIgnoreCase("kompozicija")){
            dataForConnection.setVisibilityOfTheFirstElement(connection.getVisibilityOfTheFirstElement());
            dataForConnection.setInstanceOfTheFirstElement(connection.getInstanceOfTheFirstElement());
            dataForConnection.setCardinalityOfTheFirstElement(connection.getCardinalityOfTheFirstElement());

            dataForConnection.setVisibilityOfTheSecondElement(connection.getVisibilityOfTheSecondElement());
            dataForConnection.setInstanceOfTheSecondElement(connection.getInstanceOfTheSecondElement());
            dataForConnection.setCardinalityOfTheSecondElement(connection.getCardinalityOfTheSecondElement());
        }

        return dataForConnection;
    }
    private int calculateStringWidth(String str) {
        return str.length() * CHARACTER_WIDTH;
    }

    public DiagramView getDiagramView() {
        return diagramView;
    }

    public void setDiagramView(DiagramView diagramView) {
        this.diagramView = diagramView;
    }

    public DiagramElement getDiagramElement() {
        return diagramElement;
    }

    public void setDiagramElement(DiagramElement diagramElement) {
        this.diagramElement = diagramElement;
    }

    public String getOldType() {
        return oldType;
    }

    public void setOldType(String oldType) {
        this.oldType = oldType;
    }

    public String getNewType() {
        return newType;
    }

    public void setNewType(String newType) {
        this.newType = newType;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public List<ClassContent> getOldMethods() {
        return oldMethods;
    }

    public void setOldMethods(List<ClassContent> oldMethods) {
        this.oldMethods = oldMethods;
    }

    public List<ClassContent> getNewMethods() {
        return newMethods;
    }

    public void setNewMethods(List<ClassContent> newMethods) {
        this.newMethods = newMethods;
    }

    public List<ClassContent> getOldAtributes() {
        return oldAtributes;
    }

    public void setOldAtributes(List<ClassContent> oldAtributes) {
        this.oldAtributes = oldAtributes;
    }

    public List<ClassContent> getNewAtributes() {
        return newAtributes;
    }

    public void setNewAtributes(List<ClassContent> newAtributes) {
        this.newAtributes = newAtributes;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public boolean isNothingHasBeenDone() {
        return nothingHasBeenDone;
    }

    public void setNothingHasBeenDone(boolean nothingHasBeenDone) {
        this.nothingHasBeenDone = nothingHasBeenDone;
    }
}

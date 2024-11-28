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
    private List<ClassContent> oldAttributes;
    private List<ClassContent> newAttributes;
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
            // In this object under, I have all the data from the received DiagramElement!
            if (diagramElement instanceof Interclass) {
                DataForElementFromDialog dataForElementFromDialog = extractDataFromInterclass(diagramElement);

                DataForElementFromDialog updatedData = null;
                if (dataForElementFromDialog != null) {
                    oldName = dataForElementFromDialog.getName();
                    oldType = dataForElementFromDialog.getType();
                    updatedData = diagramView.showEditDialogForInterclass(dataForElementFromDialog);
                    if(updatedData == null)
                        return;
                    newType = updatedData.getType();
                }

                if (updatedData != null) {
                    nothingHasBeenDone = false;
                    String[] rawAttributes = company.getRawAttributesFromDataFromElementFromDialog(dataForElementFromDialog);
                    String[] rawMethods = company.getRawMethodsFromDataFromElementFromDialog(dataForElementFromDialog);

                    List<ClassContent> attributes = new ArrayList<>();
                    company.refactorAttributes(rawAttributes, attributes);

                    List<ClassContent> methods = new ArrayList<>();
                    company.refactorMethods(rawMethods, methods);

                    String name = updatedData.getName();
                    if (name.isEmpty()) {
                        name = "+EmptyName";
                    }
                    newName = name;
                    diagramElement.setName(name);
                    setMethodsAndAttributesForDiagramElement(diagramElement, methods, attributes);
                    ((Interclass) diagramElement).setDimension(company.determiningTheSizeOfTheRectangle(name, attributes, methods));
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
                    if (updatedData.getType().equalsIgnoreCase("association") || updatedData.getType().equalsIgnoreCase("aggregation") || updatedData.getType().equalsIgnoreCase("composition")) {

                        String visibilityOfTheFirstElement = updatedData.getVisibilityOfTheFirstElement();
                        String instanceOfTheFirstElement = updatedData.getInstanceOfTheFirstElement();
                        String cardinalityOfTheFirstElement = updatedData.getCardinalityOfTheFirstElement();

                        String visibilityOfTheSecondElement = updatedData.getVisibilityOfTheSecondElement();
                        String instanceOfTheSecondElement = updatedData.getInstanceOfTheSecondElement();
                        String cardinalityOfTheSecondElement = updatedData.getCardinalityOfTheSecondElement();

                        ((Connection) diagramElement).setVisibilityOfTheFirstElement(visibilityOfTheFirstElement);
                        ((Connection) diagramElement).setInstanceOfTheFirstElement(instanceOfTheFirstElement);
                        ((Connection) diagramElement).setCardinalityOfTheFirstElement(cardinalityOfTheFirstElement);

                        ((Connection) diagramElement).setVisibilityOfTheSecondElement(visibilityOfTheSecondElement);
                        ((Connection) diagramElement).setInstanceOfTheSecondElement(instanceOfTheSecondElement);
                        ((Connection) diagramElement).setCardinalityOfTheSecondElement(cardinalityOfTheSecondElement);
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
                    diagramElementNode.setUserObject(diagramElement);  // updated Node
                    SwingUtilities.updateComponentTreeUI(classyTree.getTreeView());  // refresh
                }
            }
            isDone = true;
        }
        else{
            turnOldToNewMethodsAndAttributes(newName, newAttributes, newMethods);
        }
        setChangedToTrueInCurrentProject();
    }

    @Override
    public void undoCommand() {
        turnOldToNewMethodsAndAttributes(oldName, oldAttributes, oldMethods);
    }

    private void turnOldToNewMethodsAndAttributes(String oldName, List<ClassContent> oldAttributes, List<ClassContent> oldMethods) {
        diagramElement.setName(oldName);
        if(diagramElement instanceof Klasa){
            ((Klasa) diagramElement).setAttributes(oldAttributes);
            ((Klasa) diagramElement).setMethods(oldMethods);
        }
        else if(diagramElement instanceof Interfejs){
            // He has no attributes
            ((Interfejs) diagramElement).setMethods(oldMethods);
        }
        else if(diagramElement instanceof Enum){
            ((Enum) diagramElement).setAttributes(oldAttributes);
            ((Enum) diagramElement).setMethods(oldMethods);
        }
        else if(diagramElement instanceof AbstractClass){
            ((AbstractClass) diagramElement).setAttributes(oldAttributes);
            ((AbstractClass) diagramElement).setMethods(oldMethods);
        }
        ((Interclass) diagramElement).setDimension(company.determiningTheSizeOfTheRectangle(oldName, oldAttributes, oldMethods));
    }

    private void setMethodsAndAttributesForDiagramElement(DiagramElement diagramElement, List<ClassContent> methods, List<ClassContent> attributes){
        if (diagramElement instanceof Klasa) {
            oldMethods = ((Klasa) diagramElement).getMethods();
            oldAttributes = ((Klasa) diagramElement).getAttributes();
            newMethods = methods;
            newAttributes = attributes;
            ((Klasa) diagramElement).setAttributes(newAttributes);
            ((Klasa) diagramElement).setMethods(newMethods);
        } else if (diagramElement instanceof Interfejs) {
            oldMethods = ((Interfejs) diagramElement).getMethods();
            newMethods = methods;
            // nema atribute
            ((Interfejs) diagramElement).setMethods(newMethods);
        } else if (diagramElement instanceof Enum) {
            oldMethods = ((Enum) diagramElement).getMethods();
            oldAttributes = ((Enum) diagramElement).getAttributes();
            newMethods = methods;
            newAttributes = attributes;
            ((Enum) diagramElement).setAttributes(newAttributes);
            ((Enum) diagramElement).setMethods(newMethods);
        } else if (diagramElement instanceof AbstractClass) {
            oldAttributes = ((AbstractClass) diagramElement).getAttributes();
            oldMethods = ((AbstractClass) diagramElement).getMethods();
            newMethods = methods;
            newAttributes = attributes;
            ((AbstractClass) diagramElement).setAttributes(newAttributes);
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

        // Converting attributes && methods in string format
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
        if (!(diagramElement instanceof Connection)) {   // Easier casting
            return null;
        }

        Connection connection = (Connection) diagramElement;
        String type = connection.getType();

        DataForConnection dataForConnection = new DataForConnection(type);

        // If the connect had instanced (association/agg/comp), fill up the data object with that data and return
        if(type.equalsIgnoreCase("association") || type.equalsIgnoreCase("aggregation") || type.equalsIgnoreCase("composition")){
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

    public List<ClassContent> getOldAttributes() {
        return oldAttributes;
    }

    public void setOldAttributes(List<ClassContent> oldAttributes) {
        this.oldAttributes = oldAttributes;
    }

    public List<ClassContent> getNewAttributes() {
        return newAttributes;
    }

    public void setNewAttributes(List<ClassContent> newAttributes) {
        this.newAttributes = newAttributes;
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

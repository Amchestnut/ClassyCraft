package raf.dsw.classycraft.app.model.state.classesAndConnections;

import raf.dsw.classycraft.app.model.diagramElements.DiagramElement;
import raf.dsw.classycraft.app.model.diagramElements.connections.*;
import raf.dsw.classycraft.app.model.diagramElements.elements.Enum;
import raf.dsw.classycraft.app.model.diagramElements.elements.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static raf.dsw.classycraft.app.model.diagramElements.elements.Constants.*;

public class Company extends AbstractFactory{

    DiagramElement interclass = null;
    Dimension dimension = null;

    public Company() {
    }
    @Override
    public DiagramElement createInterclass(Point location, DataForElementFromDialog dataForElementFromDialog) {

        String type = dataForElementFromDialog.getType();
        String name = dataForElementFromDialog.getName();

        String[] rawAttributes = getRawAttributesFromDataFromElementFromDialog(dataForElementFromDialog);
        String[] rawMethods = getRawMethodsFromDataFromElementFromDialog(dataForElementFromDialog);

        List<ClassContent> attributes = new ArrayList<>();
        attributes = refactorAttributes(rawAttributes, attributes);
        List<ClassContent> methods = new ArrayList<>();
        methods = refactorMethods(rawMethods, methods);

        if(name.isEmpty()){
            name = "+EmptyName";
        }

        dimension = determiningTheSizeOfTheRectangle(name, attributes, methods);

        // Based on the data in dialog, we create a class/interface/enum/abstract
        if(type.equalsIgnoreCase("Class")){
            interclass = new Klasa(0xFFFF4040, 3, name, location, dimension);
            ((Klasa)interclass).setAttributes(attributes);
            ((Klasa)interclass).setMethods(methods);
        }
        else if(type.equalsIgnoreCase("Abstract Class")){
            interclass = new AbstractClass(0xFFFFFF40, 3, name, location, dimension);
            ((AbstractClass)interclass).setAttributes(attributes);
            ((AbstractClass)interclass).setMethods(methods);
        }
        else if(type.equalsIgnoreCase("Interface")){  // If the user clicked "add interface", here we create an interface
            interclass = new Interfejs(0xFF40FF40, 3, name, location, dimension);
            ((Interfejs)interclass).setMethods(methods);
        }
        else if(type.equalsIgnoreCase("Enum Class")){
            interclass = new Enum(0xFF4040FF, 3, name, location, dimension);
            ((Enum)interclass).setAttributes(attributes);
            ((Enum)interclass).setMethods(methods);
        }

        return interclass;
    }

    public List<ClassContent> refactorAttributes(String[] rawAttributes, List<ClassContent> attributes) {
        if(rawAttributes != null){
            for (String pojedinacanAtribut : rawAttributes) {
                pojedinacanAtribut = pojedinacanAtribut.trim();

                if (!pojedinacanAtribut.isEmpty()) {
                    String visibility = String.valueOf(pojedinacanAtribut.charAt(0));
                    String attributeName = pojedinacanAtribut.substring(1).trim();

                    attributes.add(new Attribute(visibility, attributeName));
                }
            }
        }
        return attributes;
    }
    public List<ClassContent> refactorMethods(String[] rawMethods, List<ClassContent> metode){
        if(rawMethods != null){
            for (String pojedinacnaMetoda : rawMethods) {
                pojedinacnaMetoda = pojedinacnaMetoda.trim();

                if (!pojedinacnaMetoda.isEmpty()) {
                    String visibility = String.valueOf(pojedinacnaMetoda.charAt(0));
                    String attributeName = pojedinacnaMetoda.substring(1).trim();

                    metode.add(new Method(visibility, attributeName));
                }
            }
        }
        return metode;
    }
    @Override
    public DiagramElement createConnection(DataForConnection dataForConnection) {

        String type = dataForConnection.getType();
        String visibilityOfTheFirstElement = dataForConnection.getVisibilityOfTheFirstElement();
        String instanceOfTheFirstElement = dataForConnection.getInstanceOfTheFirstElement();
        String kardinalnostOfTheFirstElement = dataForConnection.getCardinalityOfTheFirstElement();

        String visibilityOfTheSecondElement = dataForConnection.getVisibilityOfTheSecondElement();
        String instanceOfTheSecondElement = dataForConnection.getInstanceOfTheSecondElement();
        String kardinalnostOfTheSecondElement = dataForConnection.getCardinalityOfTheSecondElement();

        Interclass interclassFROM = dataForConnection.getInterclassFROM();
        Interclass interclassTO = dataForConnection.getInterclassTO();
        Connection connection = null;

        if(type.equalsIgnoreCase("association")){
            connection = new AssociationConnection(0xff000000,5, "Association connection", "association", interclassFROM, interclassTO);

            connection.setVisibilityOfTheFirstElement(visibilityOfTheFirstElement);
            connection.setInstanceOfTheFirstElement(instanceOfTheFirstElement);
            connection.setCardinalityOfTheFirstElement(kardinalnostOfTheFirstElement);

            connection.setVisibilityOfTheSecondElement(visibilityOfTheSecondElement);
            connection.setInstanceOfTheSecondElement(instanceOfTheSecondElement);
            connection.setCardinalityOfTheSecondElement(kardinalnostOfTheSecondElement);

        }
        else if(type.equalsIgnoreCase("inheritance")){
            connection = new InheritanceConnection(0xff000000,5, "Inheritance connection", "inheritance", interclassFROM, interclassTO);
        }
        else if(type.equalsIgnoreCase("realisation")){
            connection = new RealisationConnection(0xff000000,5, "Realisation connection", "realisation", interclassFROM, interclassTO);
        }
        else if(type.equalsIgnoreCase("dependency")){
            connection = new DependencyConnection(0xff000000,5, "Dependency connection", "dependency", interclassFROM, interclassTO);
        }
        else if(type.equalsIgnoreCase("aggregation")){
            connection = new AggregationConnection(0xff000000,5, "Aggregation connection", "aggregation", interclassFROM, interclassTO);

            connection.setVisibilityOfTheFirstElement(visibilityOfTheFirstElement);
            connection.setInstanceOfTheFirstElement(instanceOfTheFirstElement);
            connection.setCardinalityOfTheFirstElement(kardinalnostOfTheFirstElement);

            connection.setVisibilityOfTheSecondElement(visibilityOfTheSecondElement);
            connection.setInstanceOfTheSecondElement(instanceOfTheSecondElement);
            connection.setCardinalityOfTheSecondElement(kardinalnostOfTheSecondElement);
        }
        else if(type.equalsIgnoreCase("composition")){
            connection = new CompositionConnection(0xff000000,5, "Composition connection", "composition", interclassFROM, interclassTO);

            connection.setVisibilityOfTheFirstElement(visibilityOfTheFirstElement);
            connection.setInstanceOfTheFirstElement(instanceOfTheFirstElement);
            connection.setCardinalityOfTheFirstElement(kardinalnostOfTheFirstElement);

            connection.setVisibilityOfTheSecondElement(visibilityOfTheSecondElement);
            connection.setInstanceOfTheSecondElement(instanceOfTheSecondElement);
            connection.setCardinalityOfTheSecondElement(kardinalnostOfTheSecondElement);
        }

        return connection;
    }

    public Dimension determiningTheSizeOfTheRectangle(String name, List<ClassContent> attributes, List<ClassContent> methods){

        int minimumWidth = 100;
        int minimumHeight = 70;

        int counterAttribute = 0;
        int counterMethod = 0;

        int maxWidth = calculateStringWidth(name);
        for(ClassContent cc : attributes){
            counterAttribute++;
            String currentAttribute = null;
            if(cc instanceof Attribute){
                currentAttribute = ((Attribute) cc).getAttributeName();
            }
            int stringSize = 0;
            if(currentAttribute != null){
                stringSize = calculateStringWidth(currentAttribute);
            }
            maxWidth = Math.max(maxWidth, stringSize);
        }
        for(ClassContent cc : methods){
            counterMethod++;
            String currentMethod = null;
            if(cc instanceof Method){
                currentMethod = ((Method) cc).getMethodName();
            }
            int stringSize = 0;
            if(currentMethod != null){
                stringSize = calculateStringWidth(currentMethod);
            }
            maxWidth = Math.max(maxWidth, stringSize);

        }

        int maxHeight = Math.max(minimumHeight, (LINE_HEIGHT * (1 + counterAttribute + counterMethod)) + 10);
        maxWidth = Math.max(maxWidth + PADDING, minimumWidth);

        return new Dimension(maxWidth, maxHeight);
    }
    public String[] getRawAttributesFromDataFromElementFromDialog(DataForElementFromDialog dataForElementFromDialog){
        String[] rawAttributes = null;
        if(!dataForElementFromDialog.getAttributes().isEmpty() || dataForElementFromDialog.getAttributes().equalsIgnoreCase("")){  // returns null pointer exp
            rawAttributes = dataForElementFromDialog.getAttributes().split("\n");
        }
        return rawAttributes;
    }
    public String[] getRawMethodsFromDataFromElementFromDialog(DataForElementFromDialog dataForElementFromDialog){
        String[] rawMethods = null;
        if(!dataForElementFromDialog.getMethods().isEmpty() || dataForElementFromDialog.getAttributes().equalsIgnoreCase("")){
            rawMethods = dataForElementFromDialog.getMethods().split("\n");
        }
        return rawMethods;
    }

    private int calculateStringWidth(String str) {
        return str.length() * CHARACTER_WIDTH;
    }

}

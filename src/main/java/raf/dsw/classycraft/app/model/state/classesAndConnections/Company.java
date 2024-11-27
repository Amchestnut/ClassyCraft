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

        List<ClassContent> atributi = new ArrayList<>();
        atributi = refactorAttributes(rawAttributes, atributi);
        List<ClassContent> metode = new ArrayList<>();
        metode = refactorMethods(rawMethods, metode);

        if(name.isEmpty()){
            name = "+EmptyName";
        }

        dimension = odredjivanjeVelicinePravougaonika(name, atributi, metode);

        // Based on the data in dialog, we create a class/interface/enum/abstract
        if(type.equalsIgnoreCase("Class")){
            interclass = new Klasa(0xFFFF4040, 3, name, location, dimension);
            ((Klasa)interclass).setAttributes(atributi);
            ((Klasa)interclass).setMethods(metode);
        }
        else if(type.equalsIgnoreCase("Abstract Class")){
            interclass = new AbstractClass(0xFFFFFF40, 3, name, location, dimension);
            ((AbstractClass)interclass).setAttributes(atributi);
            ((AbstractClass)interclass).setMethods(metode);
        }
        else if(type.equalsIgnoreCase("Interface")){  // If the user clicked "add interface", here we create an interface
            interclass = new Interfejs(0xFF40FF40, 3, name, location, dimension);
            ((Interfejs)interclass).setMethods(metode);
        }
        else if(type.equalsIgnoreCase("Enum Class")){
            interclass = new Enum(0xFF4040FF, 3, name, location, dimension);
            ((Enum)interclass).setAttributes(atributi);
            ((Enum)interclass).setMethods(metode);
        }

        return interclass;
    }

    public List<ClassContent> refactorAttributes(String[] rawAttributes, List<ClassContent> atributi) {
        if(rawAttributes != null){
            for (String pojedinacanAtribut : rawAttributes) {
                pojedinacanAtribut = pojedinacanAtribut.trim();

                if (!pojedinacanAtribut.isEmpty()) {
                    String visibility = String.valueOf(pojedinacanAtribut.charAt(0));
                    String attributeName = pojedinacanAtribut.substring(1).trim();

                    atributi.add(new Attribute(visibility, attributeName));
                }
            }
        }
        return atributi;
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
        String kardinalnostOfTheFirstElement = dataForConnection.getKardinalnostOfTheFirstElement();

        String visibilityOfTheSecondElement = dataForConnection.getVisibilityOfTheSecondElement();
        String instanceOfTheSecondElement = dataForConnection.getInstanceOfTheSecondElement();
        String kardinalnostOfTheSecondElement = dataForConnection.getKardinalnostOfTheSecondElement();

        Interclass interclassOD = dataForConnection.getInterclassOD();
        Interclass interclassDO = dataForConnection.getInterclassDO();
        Connection connection = null;

        if(type.equalsIgnoreCase("asocijacija")){
            connection = new AssociationConnection(0xff000000,5, "Veza asocijacije", "asocijacija", interclassOD, interclassDO);

            connection.setVisibilityOfTheFirstElement(visibilityOfTheFirstElement);
            connection.setInstanceOfTheFirstElement(instanceOfTheFirstElement);
            connection.setKardinalnostOfTheFirstElement(kardinalnostOfTheFirstElement);

            connection.setVisibilityOfTheSecondElement(visibilityOfTheSecondElement);
            connection.setInstanceOfTheSecondElement(instanceOfTheSecondElement);
            connection.setKardinalnostOfTheSecondElement(kardinalnostOfTheSecondElement);

        }
        else if(type.equalsIgnoreCase("nasledjivanje")){
            connection = new InheritanceConnection(0xff000000,5, "Veza nasledjivanja", "nasledjivanje", interclassOD, interclassDO);
        }
        else if(type.equalsIgnoreCase("realizacija")){
            connection = new RealisationConnection(0xff000000,5, "Veza realizacije", "realizacija", interclassOD, interclassDO);
        }
        else if(type.equalsIgnoreCase("zavisnost")){
            connection = new DependencyConnection(0xff000000,5, "Veza zavisnosti", "zavisnost", interclassOD, interclassDO);
        }
        else if(type.equalsIgnoreCase("agregacija")){
            connection = new AggregationConnection(0xff000000,5, "Veza agregacije", "agregacija", interclassOD, interclassDO);

            connection.setVisibilityOfTheFirstElement(visibilityOfTheFirstElement);
            connection.setInstanceOfTheFirstElement(instanceOfTheFirstElement);
            connection.setKardinalnostOfTheFirstElement(kardinalnostOfTheFirstElement);

            connection.setVisibilityOfTheSecondElement(visibilityOfTheSecondElement);
            connection.setInstanceOfTheSecondElement(instanceOfTheSecondElement);
            connection.setKardinalnostOfTheSecondElement(kardinalnostOfTheSecondElement);
        }
        else if(type.equalsIgnoreCase("kompozicija")){
            connection = new CompositionConnection(0xff000000,5, "Veza kompozicije", "kompozicija", interclassOD, interclassDO);

            connection.setVisibilityOfTheFirstElement(visibilityOfTheFirstElement);
            connection.setInstanceOfTheFirstElement(instanceOfTheFirstElement);
            connection.setKardinalnostOfTheFirstElement(kardinalnostOfTheFirstElement);

            connection.setVisibilityOfTheSecondElement(visibilityOfTheSecondElement);
            connection.setInstanceOfTheSecondElement(instanceOfTheSecondElement);
            connection.setKardinalnostOfTheSecondElement(kardinalnostOfTheSecondElement);
        }

        return connection;
    }

    public Dimension odredjivanjeVelicinePravougaonika(String name, List<ClassContent> atributi, List<ClassContent> metode){

        int minimumWidth = 100;
        int minimumHeight = 70;

        int counterAttribute = 0;
        int counterMethod = 0;

        int maxWidth = calculateStringWidth(name);
        for(ClassContent cc : atributi){
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
        for(ClassContent cc : metode){
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

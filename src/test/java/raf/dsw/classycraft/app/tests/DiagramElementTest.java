package raf.dsw.classycraft.app.tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import raf.dsw.classycraft.app.model.diagramElements.elements.*;
import raf.dsw.classycraft.app.model.diagramElements.connections.Connection;
import raf.dsw.classycraft.app.model.diagramElements.elements.Enum;

import java.awt.*;
import java.util.List;

public class DiagramElementTest {

    @Test
    public void testCreateClass() {
        Point location = new Point(100, 100);
        Dimension dimension = new Dimension(200, 100);
        Klasa klasa = new Klasa(0xFFFF4040, 3, "+TestClass", location, dimension);

        assertEquals("+TestClass", klasa.getName());
        assertEquals(location, klasa.getLocation());
        assertEquals(dimension, klasa.getDimension());
        assertNotNull(klasa.getAttributes());
        assertNotNull(klasa.getMethods());
        assertTrue(klasa.getAttributes().isEmpty());
        assertTrue(klasa.getMethods().isEmpty());
    }

    @Test
    public void testAddAttributeToClass() {
        Klasa klasa = new Klasa();
        Attribute attribute = new Attribute("+", "attributeName: String");
        klasa.getAttributes().add(attribute);

        assertEquals(1, klasa.getAttributes().size());
        assertEquals(attribute, klasa.getAttributes().get(0));
    }

    @Test
    public void testAddMethodToClass() {
        Klasa klasa = new Klasa();
        Method method = new Method("-", "methodName(): void");
        klasa.getMethods().add(method);

        assertEquals(1, klasa.getMethods().size());
        assertEquals(method, klasa.getMethods().get(0));
    }

    @Test
    public void testInterclassConnections() {
        Klasa klasa1 = new Klasa();
        Klasa klasa2 = new Klasa();
        Connection connection = new Connection() {};

        klasa1.addAConnectionInThisInterclass(connection);
        List<Connection> connections = klasa1.getAllConnectionsOnThisInterclass();

        assertEquals(1, connections.size());
        assertEquals(connection, connections.get(0));

        klasa1.removeAConnectionInThisInterclass(connection);
        assertTrue(klasa1.getAllConnectionsOnThisInterclass().isEmpty());
    }

    @Test
    public void testExportClass() {
        Klasa klasa = new Klasa();
        klasa.setName("+TestClass");
        Attribute attribute = new Attribute("+", "attributeName: String");
        Method method = new Method("-", "methodName(): void");
        klasa.getAttributes().add(attribute);
        klasa.getMethods().add(method);

        String expectedExport = "public class TestClass{\n" +
                "\tpublicString attributeName;\n" +
                "\tvoid methodName()(){}\n" +
                "}";

        String exportResult = klasa.export().replaceAll("\\s+", "");
        assertEquals(expectedExport.replaceAll("\\s+", ""), exportResult);
    }

    @Test
    public void testInterfaceCreation() {
        Point location = new Point(50, 50);
        Dimension dimension = new Dimension(150, 75);
        Interfejs interfejs = new Interfejs(0xFF40FF40, 3, "+TestInterface", location, dimension);

        assertEquals("+TestInterface", interfejs.getName());
        assertEquals(location, interfejs.getLocation());
        assertEquals(dimension, interfejs.getDimension());
        assertNotNull(interfejs.getMethods());
        assertTrue(interfejs.getMethods().isEmpty());
    }

    @Test
    public void testEnumCreation() {
        Point location = new Point(30, 30);
        Dimension dimension = new Dimension(120, 60);
        Enum enumClass = new Enum(0xFF4040FF, 3, "+TestEnum", location, dimension);

        assertEquals("+TestEnum", enumClass.getName());
        assertEquals(location, enumClass.getLocation());
        assertEquals(dimension, enumClass.getDimension());
        assertNotNull(enumClass.getAttributes());
        assertNotNull(enumClass.getMethods());
        assertTrue(enumClass.getAttributes().isEmpty());
        assertTrue(enumClass.getMethods().isEmpty());
    }
}

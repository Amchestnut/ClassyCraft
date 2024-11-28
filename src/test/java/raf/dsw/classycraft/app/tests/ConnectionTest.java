package raf.dsw.classycraft.app.tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import raf.dsw.classycraft.app.model.diagramElements.connections.*;
import raf.dsw.classycraft.app.model.diagramElements.elements.Klasa;
import raf.dsw.classycraft.app.model.diagramElements.elements.Interclass;

import java.awt.*;

public class ConnectionTest {

    @Test
    public void testAssociationConnection() {
        Interclass from = new Klasa();
        Interclass to = new Klasa();
        AssociationConnection connection = new AssociationConnection(0xff000000, 5, "Association", "association", from, to);

        assertEquals("association", connection.getType());
        assertEquals(from, connection.getInterclassFROM());
        assertEquals(to, connection.getInterclassTO());
    }

    @Test
    public void testInheritanceConnection() {
        Interclass subclass = new Klasa();
        Interclass superclass = new Klasa();
        InheritanceConnection connection = new InheritanceConnection(0xff000000, 5, "Inheritance", "inheritance", subclass, superclass);

        assertEquals("inheritance", connection.getType());
        assertEquals(subclass, connection.getInterclassFROM());
        assertEquals(superclass, connection.getInterclassTO());
    }

    @Test
    public void testAggregationConnection() {
        Interclass whole = new Klasa();
        Interclass part = new Klasa();
        AggregationConnection connection = new AggregationConnection(0xff000000, 5, "Aggregation", "aggregation", whole, part);

        assertEquals("aggregation", connection.getType());
        assertEquals(whole, connection.getInterclassFROM());
        assertEquals(part, connection.getInterclassTO());
    }

    @Test
    public void testCompositionConnection() {
        Interclass composite = new Klasa();
        Interclass component = new Klasa();
        CompositionConnection connection = new CompositionConnection(0xff000000, 5, "Composition", "composition", composite, component);

        assertEquals("composition", connection.getType());
        assertEquals(composite, connection.getInterclassFROM());
        assertEquals(component, connection.getInterclassTO());
    }

    @Test
    public void testRealisationConnection() {
        Interclass implementationClass = new Klasa();
        Interclass interfaceClass = new Klasa();
        RealisationConnection connection = new RealisationConnection(0xff000000, 5, "Realisation", "realisation", implementationClass, interfaceClass);

        assertEquals("realisation", connection.getType());
        assertEquals(implementationClass, connection.getInterclassFROM());
        assertEquals(interfaceClass, connection.getInterclassTO());
    }

    @Test
    public void testDependencyConnection() {
        Interclass client = new Klasa();
        Interclass supplier = new Klasa();
        DependencyConnection connection = new DependencyConnection(0xff000000, 5, "Dependency", "dependency", client, supplier);

        assertEquals("dependency", connection.getType());
        assertEquals(client, connection.getInterclassFROM());
        assertEquals(supplier, connection.getInterclassTO());
    }

    @Test
    public void testSetConnectionPoints() {
        Connection connection = new AssociationConnection();
        connection.setConnectionPointFROM(new Point(100, 100));
        connection.setConnectionPointTO(new Point(200, 200));

        assertEquals(new Point(100, 100), connection.getConnectionPointFROM());
        assertEquals(new Point(200, 200), connection.getConnectionPointTO());
    }
}

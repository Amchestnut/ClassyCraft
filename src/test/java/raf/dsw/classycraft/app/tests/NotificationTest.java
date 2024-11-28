package raf.dsw.classycraft.app.tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import raf.dsw.classycraft.app.observer.*;
import raf.dsw.classycraft.app.model.modelImplementation.Project;

import java.util.ArrayList;
import java.util.List;

public class NotificationTest {

    class TestSubscriber implements ISubscriber {
        public List<Object> notifications = new ArrayList<>();

        @Override
        public void update(Object notification) {
            notifications.add(notification);
        }
    }

    @Test
    public void testSubscriberNotification() {
        Project project = new Project("Project1", null);
        TestSubscriber subscriber = new TestSubscriber();

        project.addSubscriber(subscriber);
        project.setAuthor("New Author");

        assertEquals(1, subscriber.notifications.size());
        Object notification = subscriber.notifications.get(0);
        assertNotNull(notification);
    }

    @Test
    public void testRemoveSubscriber() {
        Project project = new Project("Project1", null);
        TestSubscriber subscriber = new TestSubscriber();

        project.addSubscriber(subscriber);
        project.removeSubscriber(subscriber);
        project.setAuthor("New Author");

        assertTrue(subscriber.notifications.isEmpty());
    }
}

package raf.dsw.classycraft.app.gui.swing.logger;

import raf.dsw.classycraft.app.notifications.NotificationForLoggers;
import raf.dsw.classycraft.app.observer.ISubscriber;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileLogger implements ISubscriber {

    public FileLogger() {
    }

    @Override
    public void update(Object notification) {
        if(notification instanceof NotificationForLoggers) {
            File file = new File("src/main/resources/log.txt");
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
                writer.append(((NotificationForLoggers) notification).getNotification());
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

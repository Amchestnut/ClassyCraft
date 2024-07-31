package raf.dsw.classycraft.app.gui.swing.logger;

import raf.dsw.classycraft.app.notifications.NotificationForLoggers;
import raf.dsw.classycraft.app.observer.ISubscriber;

public class ConsoleLogger implements ISubscriber {

    public ConsoleLogger() {
    }

    @Override
    public void update(Object notification) {
        if(notification instanceof NotificationForLoggers){
            System.out.print(((NotificationForLoggers) notification).getNotification());
        }
    }
}

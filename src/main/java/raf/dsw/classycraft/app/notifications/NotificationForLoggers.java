package raf.dsw.classycraft.app.notifications;

import raf.dsw.classycraft.app.gui.swing.messanger.MessageType;

public class NotificationForLoggers {
    private String notification;
    private MessageType type;

    public NotificationForLoggers(String notification, MessageType type) {
        this.notification = notification;
        this.type = type;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }
}

package raf.dsw.classycraft.app.gui.swing.messanger;

import raf.dsw.classycraft.app.notifications.NotificationForLoggers;
import raf.dsw.classycraft.app.observer.IPublisher;
import raf.dsw.classycraft.app.observer.ISubscriber;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageGenerator implements IPublisher {
    private List<ISubscriber> subs;
    private String text;
    private MessageType messageType;
    private String dateAndTime;

    public MessageGenerator() {
        initialise();
    }
    private void initialise(){
        subs = new ArrayList<>();
    }
    public void generateMessage(String text, MessageType messageType){
        this.text = text;
        this.messageType = messageType;

        String[] split = LocalDate.now().toString().split("-");
        Date datumTMP = new Date();
        this.dateAndTime = "[" + split[2] + "." + split[1] + "." + split[0] + "][" + datumTMP.getHours() + ":" + datumTMP.getMinutes() + "]";

        notifySubscribers(new NotificationForLoggers(toString(), getMessageType()));
    }
    @Override
    public String toString() {
        return "[" + messageType.toString() + "]" + dateAndTime + " " + text + "\n";
    }

    @Override
    public void addSubscriber(ISubscriber iSubscriber) {
        this.subs.add(iSubscriber);
    }

    @Override
    public void removeSubscriber(ISubscriber iSubscriber) {
        this.subs.remove(iSubscriber);
    }

    @Override
    public void notifySubscribers(Object notification) {
        for(ISubscriber subscriber : subs ){
            subscriber.update(notification);
        }
    }
    public String getText() {
        return text;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }
}

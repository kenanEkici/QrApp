package be.pxl.it.jms;


import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

@Component
public class Consumer {
    public static int receivedMessages = 0;
    public static int getReceivedMessages(){
        return receivedMessages;
    }
    public static void resetCounter(){
        receivedMessages = 0;
    }

    @JmsListener(destination = "loggingQueue")
    public void receiveQueue(Message message) throws InterruptedException, JMSException{
        TextMessage textMessage = (TextMessage) message;
        System.out.println("_________________- " + textMessage.getText() + " ______________");
        receivedMessages++;
    }
}

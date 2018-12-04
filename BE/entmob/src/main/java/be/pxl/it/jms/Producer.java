package be.pxl.it.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.time.LocalDateTime;

@Component
public class Producer {

    @Autowired
    JmsTemplate jmsTemplate;



    public void SendMessage(String message) {
        jmsTemplate.send("loggingQueue", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(message);
            }
        });
    }
}

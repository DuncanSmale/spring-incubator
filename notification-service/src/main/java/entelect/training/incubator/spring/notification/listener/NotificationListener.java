package entelect.training.incubator.spring.notification.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import entelect.training.incubator.spring.notification.sms.client.SmsClient;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.HashMap;
import java.util.Map;

@Component
public class NotificationListener {

    private SmsClient smsClient;

    public NotificationListener(SmsClient smsClient) {
        this.smsClient = smsClient;
    }

    @JmsListener(destination = "outbound.queue")
    public void receiveMessage(final Message jsonMessage) throws JMSException, JsonProcessingException {
        System.out.println("Received message " + jsonMessage);
        String phoneNumber = jsonMessage.getStringProperty("phoneNumber");
        String message = jsonMessage.getStringProperty("message");
        System.out.println(phoneNumber);
        System.out.println(message);
        smsClient.sendSms(phoneNumber, message);

        jsonMessage.acknowledge();
    }
}

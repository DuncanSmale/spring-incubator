package entelect.training.incubator.spring.notification.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import entelect.training.incubator.spring.notification.sms.client.SmsClient;
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

    @JmsListener(destination = "outbound.queue", containerFactory = "myFactory")
    public void receiveMessage(final Map<String, String> messageMap) throws JMSException, JsonProcessingException {
        System.out.println("Received message " + messageMap);
        String phoneNumber = messageMap.get("phoneNumber");
        String message = messageMap.get("message");
        smsClient.sendSms(phoneNumber, message);
    }
}

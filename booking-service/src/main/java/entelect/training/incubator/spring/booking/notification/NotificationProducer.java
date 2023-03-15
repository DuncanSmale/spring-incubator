package entelect.training.incubator.spring.booking.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import entelect.training.incubator.spring.booking.model.Customer;
import entelect.training.incubator.spring.booking.model.Flight;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class NotificationProducer {

    private JmsTemplate jmsTemplate;

    public NotificationProducer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendNotification(String phoneNumber, String messageText) throws JsonProcessingException {
        Map<String, String> payload = new HashMap<>();
        payload.put("phoneNumber", phoneNumber);
        payload.put("message", messageText);
        jmsTemplate.convertAndSend("outbound.queue", payload);
    }

    public String createNotificationMessage(Customer customer, Flight flight) {
        return String.format("Molo Air: Confirming flight %s booked for %s %s on %s.",
                flight.getFlightNumber(),
                customer.getFirstName(),
                customer.getLastName(),
                flight.getDepartureTime().toLocalDate().toString()
        );
    }
}

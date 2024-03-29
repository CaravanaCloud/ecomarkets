package ecomarkets.twilio;

import jakarta.enterprise.inject.Produces;

public class MessageProducer {
    @Produces
    public Message getMessageService() {
        return new MessageService();
    }
}

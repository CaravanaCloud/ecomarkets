package ecomarkets.domain.notification.email;

import ecomarkets.domain.core.basket.event.BasketDeliveredEvent;
import ecomarkets.domain.core.basket.event.BasketEvent;
import ecomarkets.domain.core.basket.event.BasketReservedEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class BasketEventHandler {

    public void storeReservedEvent(@Observes BasketReservedEvent event) {
        saveEmailPending(event);
    }
    public void storeDeliveredEvent(@Observes BasketDeliveredEvent event) {
        saveEmailPending(event);
    }
    public void storePayedEvent(@Observes BasketDeliveredEvent event) {
        saveEmailPending(event);
    }

    private void saveEmailPending(BasketEvent basketEvent){
        basketEvent.persist();
        EmailPendingToSend emailPendingToSend = new EmailPendingToSend(basketEvent.basketEventId());
        emailPendingToSend.persist();
    }

}


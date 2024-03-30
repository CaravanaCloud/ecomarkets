package ecomarkets.core.domain.notification.email;

import ecomarkets.core.domain.core.basket.event.BasketDeliveredEvent;
import ecomarkets.core.domain.core.basket.event.BasketEvent;
import ecomarkets.core.domain.core.basket.event.BasketReservedEvent;
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
        EmailPendingToSend emailPendingToSend = new EmailPendingToSend(basketEvent.basketEventId());
        emailPendingToSend.persist();
    }

}


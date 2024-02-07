package ecomarkets.domain.core.basket;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class BasketEventHandler {

    public void storeEvent(@Observes BasketEvent event) {
        event.persist();
    }

}

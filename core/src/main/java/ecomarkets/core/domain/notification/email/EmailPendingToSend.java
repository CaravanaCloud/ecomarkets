package ecomarkets.core.domain.notification.email;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.Immutable;

import ecomarkets.core.domain.core.basket.event.BasketEvent;
import ecomarkets.core.domain.core.basket.event.BasketEventId;

@Entity
@Immutable
public class EmailPendingToSend extends PanacheEntity {

    private BasketEventId basketEventId;

    public EmailPendingToSend(BasketEventId basketEvent){
        this();
        this.basketEventId = basketEvent;
    }

    private EmailPendingToSend(){}
    public BasketEventId getBasketEventId() {
        return basketEventId;
    }
}

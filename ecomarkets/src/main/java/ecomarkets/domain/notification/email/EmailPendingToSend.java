package ecomarkets.domain.notification.email;

import ecomarkets.domain.core.basket.event.BasketEvent;
import ecomarkets.domain.core.basket.event.BasketEventId;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.Immutable;

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

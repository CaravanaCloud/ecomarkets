package ecomarkets.domain.core.basket;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class BasketEvent extends PanacheEntity {
    public enum EventType{
        RESERVED,
        DELIVERED
    }

    private BasketId basketId;

    private EventType type;

    private BasketEvent(){}

    public BasketEvent(BasketId basketId, EventType type) {
        this.basketId = basketId;
        this.type = type;
    }

    public BasketId getBasketId() {
        return basketId;
    }

    public EventType getType() {
        return type;
    }
}

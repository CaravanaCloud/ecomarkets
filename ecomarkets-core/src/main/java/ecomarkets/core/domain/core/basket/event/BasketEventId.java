package ecomarkets.core.domain.core.basket.event;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record BasketEventId (@Column(name = "basket_event_id") Long id){
    public static BasketEventId of(Long id){
        return new BasketEventId(id);
    }
}
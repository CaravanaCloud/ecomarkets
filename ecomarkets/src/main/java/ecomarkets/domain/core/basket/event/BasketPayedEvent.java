package ecomarkets.domain.core.basket.event;

import com.google.errorprone.annotations.Immutable;
import ecomarkets.domain.core.basket.BasketId;
import jakarta.persistence.Entity;

@Entity
@Immutable
public class BasketPayedEvent extends BasketEvent {
    private BasketPayedEvent(){}
    public BasketPayedEvent(BasketId basketId){
        super(basketId);
    }
}

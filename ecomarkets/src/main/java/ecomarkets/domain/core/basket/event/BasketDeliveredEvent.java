package ecomarkets.domain.core.basket.event;


import com.google.errorprone.annotations.Immutable;
import ecomarkets.domain.core.basket.BasketId;
import jakarta.persistence.Entity;

@Entity
@Immutable
public class BasketDeliveredEvent extends BasketEvent {
    private BasketDeliveredEvent(){}
    public BasketDeliveredEvent(BasketId basketId){
        super(basketId);
    }
}

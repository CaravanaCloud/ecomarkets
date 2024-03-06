package ecomarkets.domain.core.basket.event;


import com.google.errorprone.annotations.Immutable;
import ecomarkets.domain.core.basket.BasketId;
import jakarta.persistence.Entity;

@Entity
@Immutable
public class BasketReservedEvent extends BasketEvent {

    private BasketReservedEvent(){}
    public BasketReservedEvent(BasketId basketId){
        super(basketId);
    }
}

package ecomarkets.domain.core.basket.event;

import ecomarkets.domain.core.basket.BasketId;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class BasketEvent extends PanacheEntity {
    private BasketId basketId;
    private LocalDateTime creationDate;
    BasketEvent(){}
    public BasketEvent(BasketId basketId) {
        this.basketId = basketId;
        this.creationDate = LocalDateTime.now();
    }
    public BasketId getBasketId() {
        return basketId;
    }
    public LocalDateTime getCreationDate() {return creationDate;}
    public BasketEventId basketEventId(){
        return BasketEventId.of(id);
    }
}

package ecomarkets.domain.core.fair;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
public class Fair extends PanacheEntity {

    private ShoppingPeriod shoppingPeriod;

    private LocalDateTime creationDate;

    private Fair(){}

    public Fair(ShoppingPeriod shoppingPeriod) {
        this.shoppingPeriod = shoppingPeriod;
        this.creationDate = LocalDateTime.now();
    }

    public ShoppingPeriod getShoppingPeriod() {
        return shoppingPeriod;
    }

    public FairId fairId(){
        return FairId.of(id);
    }


}

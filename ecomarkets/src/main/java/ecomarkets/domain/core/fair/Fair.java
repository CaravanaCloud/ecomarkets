package ecomarkets.domain.core.fair;

import ecomarkets.domain.core.farmer.FarmerId;
import ecomarkets.domain.core.product.ProductId;
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

    public static Fair of(ShoppingPeriod shoppingPeriod) {
        return new Fair(shoppingPeriod);
    }

    public ShoppingPeriod getShoppingPeriod() {
        return shoppingPeriod;
    }

    public FairId fairId(){
        return FairId.of(id);
    }

    public FarmerProductAvailableInFair addProduct(FarmerId farmerId, ProductId productId, Integer amount){
        FarmerProductAvailableInFair farmerProductAvailableInFair = FarmerProductAvailableInFair.of(
                fairId(),
                farmerId,
                productId,
                amount);
        farmerProductAvailableInFair.persist();
        return farmerProductAvailableInFair;
    }

}

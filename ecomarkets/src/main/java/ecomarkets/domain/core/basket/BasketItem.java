package ecomarkets.domain.core.basket;

import java.time.LocalDateTime;

import ecomarkets.domain.core.product.ProductId;
import jakarta.persistence.Embeddable;

@Embeddable
public record BasketItem (ProductId productId,
        Integer amount,
        LocalDateTime creationDate){

    public static BasketItem of(ProductId productId, Integer amount){
        BasketItem bi = new BasketItem(productId, amount, LocalDateTime.now());
        return bi;
    }

}

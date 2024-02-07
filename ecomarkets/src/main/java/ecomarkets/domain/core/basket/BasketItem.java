package ecomarkets.domain.core.basket;

import java.time.LocalDateTime;

import ecomarkets.domain.core.product.ProductId;
import jakarta.persistence.Embeddable;

@Embeddable
public record BasketItem (ProductId productId,
        Integer amount,
        Double totalPayment,
        LocalDateTime creationDate){

    public static BasketItem of(ProductId productId, Integer amount, Double totalPayment){
        BasketItem bi = new BasketItem(productId, amount, totalPayment, LocalDateTime.now());
        return bi;
    }

}

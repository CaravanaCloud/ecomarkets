package ecomarkets.domain.core.basket;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import ecomarkets.domain.core.product.ProductId;
import jakarta.persistence.Embeddable;

@Embeddable
public record BasketItem (ProductId productId,
        Integer amount,
        BigDecimal totalPayment,
        LocalDateTime creationDate){

    public static BasketItem of(ProductId productId, Integer amount, BigDecimal totalPayment){
        BasketItem bi = new BasketItem(productId, amount, totalPayment, LocalDateTime.now());
        return bi;
    }

}

package ecomarkets.rs.basket.form;

import ecomarkets.core.domain.core.product.ProductId;

public record BasketItemForm(ProductId productId,
                             Integer amount){

    public static BasketItemForm of(ProductId productId, Integer amount){
        BasketItemForm bi = new BasketItemForm(productId, amount);
        return bi;
    }

}
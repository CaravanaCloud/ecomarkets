package ecomarkets.rs.basket;

import ecomarkets.domain.core.product.ProductId;

public record BasketItemDTO (ProductId productId,
                          Integer amount){

    public static BasketItemDTO of(ProductId productId, Integer amount){
        BasketItemDTO bi = new BasketItemDTO(productId, amount);
        return bi;
    }

}
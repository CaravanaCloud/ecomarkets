package ecomarkets.domain.core.basket;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record BasketId (@Column(name = "basket_id", nullable = false) Long id){

    public static BasketId of(Long id) {
        return new BasketId(id);
    }

}

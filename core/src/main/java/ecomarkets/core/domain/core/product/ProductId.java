package ecomarkets.core.domain.core.product;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record ProductId (@Column(name = "product_id") Long id){

    public static ProductId of(Long id){
        return new ProductId(id);
    }
}

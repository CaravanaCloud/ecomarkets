package ecomarkets.core.domain.core.product.event;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

import ecomarkets.core.domain.core.product.ProductId;

@Entity
public class ProductSoldEvent extends PanacheEntity {

    private ProductId productId;

    private LocalDateTime creationDate;

    private ProductSoldEvent(){}

    public ProductSoldEvent(ProductId productId) {
        this.productId = productId;
        this.creationDate = LocalDateTime.now();
    }

    public ProductId getProductId() {
        return productId;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }
}

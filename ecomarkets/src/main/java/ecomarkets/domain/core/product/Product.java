package ecomarkets.domain.core.product;

import com.google.errorprone.annotations.Immutable;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
@Immutable
public class Product extends PanacheEntity {

    private String name;

    private Product(){}

    public static final Product of(String name){
        var product = new Product();
        product.name = name;
        return product;
    }

    public String getName() {
        return name;
    }
    
}

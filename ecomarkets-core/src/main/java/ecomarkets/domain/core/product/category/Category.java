package ecomarkets.domain.core.product.category;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Category extends PanacheEntity{

    @NotBlank
    public String name;

    private Category(){}

    public static Category of(String name){
        Category category = new Category();
        category.name = name;
        return category;
    }

}

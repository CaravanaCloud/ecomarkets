package ecomarkets.core.domain.core.product.category;

import ecomarkets.core.domain.core.product.Product;
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

    public void delete(){
        long count = Product.count("category", this);
        if(count > 0){
            throw new IllegalStateException("category with products can't be removed!");
        }

        super.delete();
    }

}

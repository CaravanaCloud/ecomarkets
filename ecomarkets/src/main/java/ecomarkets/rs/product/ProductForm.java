package ecomarkets.rs.product;


import ecomarkets.domain.core.product.*;
import ecomarkets.domain.core.product.category.Category;

public record ProductForm(String name,
                          String description,
                          RecipeIngredients recipeIngredients,
                          MeasureUnit measureUnit,
                          Price price,
                          Category category) {

    public Product parse(){
       return new ProductBuilder()
               .name(name)
               .description(description)
               .recipeIngredients(recipeIngredients.description())
               .measureUnit(measureUnit)
               .price(price.unit(), price.cents())
               .category(category).create();
    }
}

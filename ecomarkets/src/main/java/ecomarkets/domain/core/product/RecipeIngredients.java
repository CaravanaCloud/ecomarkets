package ecomarkets.domain.core.product;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record RecipeIngredients(@Column(name = "recipe_description") String description) {

    public static RecipeIngredients of(String description){
        return new RecipeIngredients(description);
    }

}

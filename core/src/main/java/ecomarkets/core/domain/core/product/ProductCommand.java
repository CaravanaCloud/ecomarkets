package ecomarkets.core.domain.core.product;

import ecomarkets.core.domain.core.product.category.Category;

public interface ProductCommand {

    String getName();

    String getDescription();

    String getIngredients();

    MeasureUnit getMeasureUnit();

    Price getPrice();

    Category getCategory();

}

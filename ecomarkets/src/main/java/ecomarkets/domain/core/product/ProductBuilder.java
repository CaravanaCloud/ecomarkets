package ecomarkets.domain.core.product;

import ecomarkets.domain.core.product.category.Category;

public class ProductBuilder {

    private String name;
    
    private String description;

    private RecipeIngredients recipeIngredients;

    private MeasureUnit unit;

    private Price price;

    private Category category;

    public ProductBuilder name(String name){
        this.name = name;
        return this;
    }

    public ProductBuilder description(String description){
        this.description = description;
        return this;
    }
    
    public ProductBuilder recipeIngredients(String recipeIngredients){
        this.recipeIngredients = RecipeIngredients.of(recipeIngredients);
        return this;
    }
   
    public ProductBuilder measureUnit(MeasureUnit unit){
        this.unit = unit;
        return this;
    }
    
    public ProductBuilder price(Integer unit, Integer cents){
        this.price = Price.of(unit, cents);
        return this;
    }
 
    public ProductBuilder category(Category category){
        this.category = category;
        return this;
    }

    public Product create(){
        return Product.of(name, description, recipeIngredients, unit, price, category);
    }
}

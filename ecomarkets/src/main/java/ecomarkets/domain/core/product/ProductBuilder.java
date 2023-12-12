package ecomarkets.domain.core.product;

public class ProductBuilder {

    private String name;
    
    private String description;

    private RecipeIngredients recipeIngredients;

    private MeasureUnit unit;

    private Price price;

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

    public Product create(){
        return Product.of(name, description, recipeIngredients, unit, price);
    }
}

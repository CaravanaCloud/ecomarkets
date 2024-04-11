package ecomarkets.vdn.view.product;

import ecomarkets.core.domain.core.product.MeasureUnit;
import ecomarkets.core.domain.core.product.Price;
import ecomarkets.core.domain.core.product.ProductCommand;
import ecomarkets.core.domain.core.product.category.Category;

public class ProductDTO implements ProductCommand {

    private Long id;
    private String name;
    private String description;
    private MeasureUnit measureUnit;

    private Double priceValue;

    private Category category;

    private String recipeIngredients;

    private ImageFormData imageFormData;

    public ProductDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String getIngredients() {
        return this.recipeIngredients;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MeasureUnit getMeasureUnit() {
        return measureUnit;
    }

    @Override
    public Price getPrice() {
        Double integerPart = Math.floor(getPriceValue());
        Double decimalPart = getPriceValue() - integerPart;
        return new Price(integerPart.intValue(), decimalPart.intValue());
    }

    public void setMeasureUnit(MeasureUnit measureUnit) {
        this.measureUnit = measureUnit;
    }

    public Double getPriceValue() {
        return priceValue;
    }

    public void setPriceValue(Double priceValue) {
        this.priceValue = priceValue;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getRecipeIngredients() {
        return recipeIngredients;
    }

    public void setRecipeIngredients(String recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ImageFormData getImageFormData() {
        return imageFormData;
    }

    public void setImageFormData(ImageFormData imageFormData) {
        this.imageFormData = imageFormData;
    }
}

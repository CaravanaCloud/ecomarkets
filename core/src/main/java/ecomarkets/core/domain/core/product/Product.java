package ecomarkets.core.domain.core.product;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

import java.util.UUID;

import ecomarkets.core.domain.core.product.category.Category;
import ecomarkets.core.domain.core.product.image.ProductImage;
import ecomarkets.core.domain.core.product.image.ProductImageBuilder;

@Entity
public class Product extends PanacheEntity {

    private String name;
    
    private String description;

    private RecipeIngredients recipeIngredients;

    private MeasureUnit measureUnit;

    private Price price;

    @ManyToOne
    private Category category;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private ProductImage productImage;

    private Product(){}

    public static final Product of(String name, 
    String description,
    RecipeIngredients recipeIngredients,
    MeasureUnit measureUnit,
    Price price,
    Category category){
        var product = new Product();
        product.name = name;
        product.description = description;
        product.recipeIngredients = recipeIngredients;
        product.measureUnit = measureUnit;
        product.price = price;
        product.category = category;
        return product;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public RecipeIngredients getRecipeIngredients() {
        return this.recipeIngredients;
    }
    
    public MeasureUnit getMeasureUnit() {
        return this.measureUnit;
    }

    public Price getPrice() {
        return this.price;
    }

    public ProductId productId(){
        return ProductId.of(id);
    }
    
    public String getCategoryName(){
        if(this.category == null){
            return "OUTROS";
        }
        return this.category.name;
    }

    public Category getCategory(){
        return this.category;
    }

    public void changeProduct(ProductCommand productCommand){
        this.name = productCommand.getName();
        this.category = productCommand.getCategory();
        this.price = productCommand.getPrice();
        this.measureUnit = productCommand.getMeasureUnit();
        this.description = productCommand.getDescription();
        this.recipeIngredients = RecipeIngredients.of(productCommand.getIngredients());
    }

    public ProductImage productImage(){
        return this.productImage;
    }

    public ProductImage newImage(String bucketName,
                                 String fileName,
                                 String mimeType){
        if(id == null){
            throw new IllegalStateException("product not persisted!");
        }
        String key;

        if(productImage != null){
            key = productImage.key();
        }else{
            key = UUID.randomUUID().toString();
        }

        this.productImage = ProductImageBuilder
                .newInstance()
                .withBucket(bucketName)
                .withKey(key)
                .withFileName(fileName)
                .withMimeType(mimeType)
                .withTag("productId", this.id.toString())
                .withTag("productName", this.name)
                .withTag("fileName", fileName)
                .build();

        return productImage;
    }

}

package ecomarkets.core.domain.usecase;

import ecomarkets.core.domain.core.basket.Basket;
import ecomarkets.core.domain.core.product.Product;
import ecomarkets.core.domain.core.product.ProductCommand;
import ecomarkets.core.domain.core.product.ProductId;
import ecomarkets.core.domain.core.product.RecipeIngredients;
import ecomarkets.core.domain.core.product.image.ImageRepository;
import ecomarkets.core.domain.core.product.image.ProductImage;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ProductUseCase {

    @Inject
    ImageRepository imageRepository;

    @Transactional
    public ProductId newProduct(ProductCommand productCommand){
        Product product = Product.of(productCommand.getName(), productCommand.getDescription(),
                RecipeIngredients.of(productCommand.getIngredients()),productCommand.getMeasureUnit(), productCommand.getPrice(), productCommand.getCategory());
        product.persist();
        return product.productId();
    }
    @Transactional
    public void changeProduct(ProductId id, ProductCommand productCommand){
        Product product = Product.findById(id.id());
        product.changeProduct(productCommand);
    }

    @Transactional
    public void newImage(ProductId productId, ImageData imageData){
        Product product = Product.findById(productId.id());
        ProductImage pm = product.newImage(imageRepository.getBucketName(),
                imageData.getFileName(),
                imageData.getMimeType());
        imageRepository.save(imageData.getFile().toPath(), pm);
    }

    @Transactional
    public void deleteProduct(ProductId productId){

        //TODO Add validation before delete product

        Product.deleteById(productId.id());

    }


}

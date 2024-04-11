package ecomarkets.core.domain.usecase;

import ecomarkets.core.domain.core.product.Product;
import ecomarkets.core.domain.core.product.ProductCommand;
import ecomarkets.core.domain.core.product.RecipeIngredients;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ProductUseCase {

    @Transactional
    public void newProduct(ProductCommand productCommand){
        Product product = Product.of(productCommand.getName(), productCommand.getDescription(),
                RecipeIngredients.of(productCommand.getIngredients()),productCommand.getMeasureUnit(), productCommand.getPrice(), productCommand.getCategory());
        product.persist();
    }
    @Transactional
    public void changeProduct(Long id, ProductCommand productCommand){
        Product product = Product.findById(id);
        product.changeProduct(productCommand);
    }
}

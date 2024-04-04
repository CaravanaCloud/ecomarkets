package ecomarkets.resources;

import ecomarkets.core.domain.core.product.Product;
import ecomarkets.core.domain.core.product.category.Category;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/products")
public class ProductResource {
    @Inject
    Template products;
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public TemplateInstance get() {
        List<Product> productList = Product.findAll().list();
        List<Category> categories = Category.findAll().list();
        return products.data("products", productList, "categories", categories);
    }


}

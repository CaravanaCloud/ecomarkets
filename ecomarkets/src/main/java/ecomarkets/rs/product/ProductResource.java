package ecomarkets.rs.product;

import java.util.List;

import ecomarkets.domain.core.product.Product;
import ecomarkets.domain.core.product.ProductBuilder;
import ecomarkets.domain.core.product.MeasureUnit;
import io.quarkus.panache.common.Sort;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/product")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {

    @Transactional
    public void init(@Observes StartupEvent event) {
        System.out.println("Initializing test database...");
        new ProductBuilder().
        name("Bolo de Banana").
        description("Bolo de Banana Fitness (Zero Glúten e Lactose)").
        recipeIngredients("Banana, aveia, Chocolate em pó 50% canela em pó Ovos, granola Açúcar mascavo, Fermento em pó").
        measureUnit(MeasureUnit.UNIT).
        price(10, 50).create().persist();
    }

    @GET
    public List<Product> getProducts() {
        return Product.listAll(Sort.ascending("name"));
    }
    
    @POST
    @Transactional
    public Response create(Product product) {
        product.persist();
        return Response
        .status(Response.Status.CREATED)
        .entity(product)
        .build();
    }

}

package ecomarkets.rs;

import java.util.List;

import ecomarkets.domain.core.product.Product;
import io.quarkus.panache.common.Sort;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/product")
public class ProductResource {

    @Transactional
    public void init(@Observes StartupEvent event) {
        System.out.println("Initializing test database...");
        Product.of("Apples").persist();
        Product.of("Oranges").persist();
        Product.of("Bananas").persist();
    }

    @Inject
    EntityManager em;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> getProducts() {
        return Product.listAll(Sort.ascending("name"));
    }
    
    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Product product) {
        product.persist();
        return Response
        .status(Response.Status.CREATED)
        .entity(product)
        .build();
    }

}

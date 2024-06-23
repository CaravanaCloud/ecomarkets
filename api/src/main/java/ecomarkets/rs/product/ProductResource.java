package ecomarkets.rs.product;

import ecomarkets.core.domain.core.product.Product;
import ecomarkets.rs.product.form.ProductForm;

import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;

import java.util.Collections;
import java.util.List;

@Path("/product")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {

    private static final Logger LOG = Logger.getLogger(ProductResource.class);
    
    @Inject
    MeterRegistry resgistry;

    
    @GET
    public List<Product> getProducts() {

        List<Product> products = Product.listAll(Sort.ascending("name"));

        LOG.info(products.get(0));
        resgistry.counter("product_count", Collections.singletonList(Tag.of("app", "ecomarkets"))).increment();

        return Product.listAll(Sort.ascending("name"));
        
    }
    
    @POST
    @Transactional
    public Response create(ProductForm productForm) {

        Product result = productForm.parse();
        result.persist();

        return Response
        .status(Response.Status.CREATED)
        .entity(result)
        .build();
    }

    @Path("/{id}")
    @GET
    public Product getProduct(@PathParam("id") Long id){
        Product result = Product.findById(id);

        if (result == null) {
            throw new NotFoundException("Product with id %d not found.".formatted(id));
        }

        return Product.findById(id);
    }

}

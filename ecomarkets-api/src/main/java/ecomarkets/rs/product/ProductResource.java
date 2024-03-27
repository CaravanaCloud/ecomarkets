package ecomarkets.rs.product;

import ecomarkets.domain.core.product.Product;
import ecomarkets.rs.product.form.ProductForm;
import io.quarkus.panache.common.Sort;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.util.List;

@Path("/product")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {

    private static final Logger LOG = Logger.getLogger(ProductResource.class);

    @GET
    public List<Product> getProducts() {
        LOG.info("hoy");
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

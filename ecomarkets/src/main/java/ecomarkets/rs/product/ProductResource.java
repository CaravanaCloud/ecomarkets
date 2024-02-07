package ecomarkets.rs.product;

import ecomarkets.domain.core.product.Product;
import ecomarkets.domain.core.product.ProductId;
import ecomarkets.domain.core.product.ProductStock;
import io.quarkus.panache.common.Sort;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/product")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {

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

    @Path("/{id}")
    @GET
    public Product getProduct(@PathParam("id") Long id){
        Product result = Product.findById(id);

        if (result == null) {
            throw new NotFoundException("Product with id %d not found.".formatted(id));
        }

        return Product.findById(id);
    }

    @Path("/{id}/stock")
    @GET
    public Double getAvailableStock(@PathParam("id") Long id){
        getProduct(id);
        return ProductStock.getAvailableStock(ProductId.of(id));
    }

}

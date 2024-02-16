package ecomarkets.rs.fair;

import ecomarkets.domain.core.fair.Fair;
import ecomarkets.domain.core.fair.FairId;
import ecomarkets.domain.core.fair.ProductAvailableInFair;
import ecomarkets.domain.core.fair.ShoppingPeriod;
import ecomarkets.domain.core.product.Product;
import ecomarkets.domain.core.product.ProductId;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/fair")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class FairResource {

    @Path("/{fairId}/product/{productId}")
    @GET
    public Double getAmountProductAvailable(@PathParam("fairId") Long fairId, @PathParam("productId") Long productId){
        Product product = Product.findById(productId);

        if (product == null) {
            throw new NotFoundException("Product with id %d not found.".formatted(productId));
        }

        Fair fair = Fair.findById(fairId);

        if (fair == null) {
            throw new NotFoundException("fair with id %d not found.".formatted(fairId));
        }

        return ProductAvailableInFair.getAmountProductAvailable(FairId.of(fairId), ProductId.of(productId));
    }

    @Path("/{fairId}/product/{productId}")
    @POST
    public ProductAvailableInFair addProduct(@PathParam("fairId") Long fairId, @PathParam("productId") Long productId, AddProductForm addProductForm){
        Fair fair = find(fairId);
        ProductAvailableInFair result = fair.addProduct(addProductForm.farmerId(), ProductId.of(productId), addProductForm.amount());
        return result;
    }

    @Path("/{fairId}")
    @GET
    public Fair find(@PathParam("fairId") Long fairId){
        Fair fair = Fair.findById(fairId);

        if (fair == null) {
            throw new NotFoundException("fair with id %d not found.".formatted(fairId));
        }

        return fair;
    }

    @POST
    public Response create(ShoppingPeriod shoppingPeriod){
        Fair fair = Fair.of(shoppingPeriod);

        return Response
                .status(Response.Status.CREATED)
                .entity(fair)
                .build();
    }
}

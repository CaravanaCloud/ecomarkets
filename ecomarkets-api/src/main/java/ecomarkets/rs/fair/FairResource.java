package ecomarkets.rs.fair;

import ecomarkets.core.domain.core.fair.*;
import ecomarkets.core.domain.core.product.Product;
import ecomarkets.core.domain.core.product.ProductId;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/fair")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class FairResource {

    //TODO: Fix injection @Inject
    ProductStock productStockService;

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

        return productStockService.getAmountProductAvailable(FairId.of(fairId), ProductId.of(productId));
    }

    @Path("/{fairId}/product/{productId}")
    @POST
    public FarmerProductAvailableInFair addProduct(@PathParam("fairId") Long fairId, @PathParam("productId") Long productId, AddProductForm addProductForm){
        Fair fair = find(fairId);
        FarmerProductAvailableInFair result = fair.addProduct(addProductForm.farmerId(), ProductId.of(productId), addProductForm.amount());
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

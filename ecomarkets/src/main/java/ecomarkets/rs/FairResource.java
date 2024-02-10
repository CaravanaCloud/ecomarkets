package ecomarkets.rs;

import ecomarkets.domain.core.fair.Fair;
import ecomarkets.domain.core.fair.FairId;
import ecomarkets.domain.core.ProductAvailableInFair;
import ecomarkets.domain.core.product.Product;
import ecomarkets.domain.core.product.ProductId;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/fair")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FairResource {

    @Path("/{fairId}/stock/{productId}")
    @GET
    public Double getAvailableStock(@PathParam("fairId") Long fairId, @PathParam("productId") Long productId){
        Product product = Product.findById(productId);

        if (product == null) {
            throw new NotFoundException("Product with id %d not found.".formatted(productId));
        }

        Fair fair = Fair.findById(fairId);

        if (fair == null) {
            throw new NotFoundException("fair with id %d not found.".formatted(fairId));
        }

        return ProductAvailableInFair.getAvailableStock(FairId.of(fairId), ProductId.of(productId));
    }
}

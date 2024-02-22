package ecomarkets.domain.core.fair.report;

import ecomarkets.domain.core.fair.FairId;
import ecomarkets.domain.core.farmer.FarmerId;
import ecomarkets.domain.core.product.ProductId;

public record FarmerProductionToDeliver(FairId fairId, FarmerId farmerId, ProductId productId, Integer amountToDeliver){

    public static FarmerProductionToDeliver of(FairId fairId, FarmerId farmerId, ProductId productId, Integer totalAmount){
        return new FarmerProductionToDeliver(fairId, farmerId, productId, totalAmount);
    }
}

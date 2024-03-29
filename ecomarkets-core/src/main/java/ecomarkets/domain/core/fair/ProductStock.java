package ecomarkets.domain.core.fair;

import ecomarkets.domain.core.product.ProductId;

public interface ProductStock {

    Double getAmountProductAvailable(FairId fairId, ProductId productId);

}

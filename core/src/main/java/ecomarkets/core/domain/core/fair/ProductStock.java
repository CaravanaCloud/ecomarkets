package ecomarkets.core.domain.core.fair;

import ecomarkets.core.domain.core.product.ProductId;

public interface ProductStock {

    Double getAmountProductAvailable(FairId fairId, ProductId productId);

}

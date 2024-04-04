package ecomarkets.core.domain.core.fair;

import ecomarkets.core.domain.core.product.ProductId;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
class ProductStockService implements ProductStock{

        @Inject
        private EntityManager entityManager;

        @Override
        public Double getAmountProductAvailable(FairId fairId, ProductId productId){
            return (Double) entityManager.createNamedQuery("FarmerProductAvailableInFair.amountProductAvailable")
                    .setParameter("fairId", fairId.id())
                    .setParameter("productId", productId.id())
                    .getSingleResult();
        }
    }
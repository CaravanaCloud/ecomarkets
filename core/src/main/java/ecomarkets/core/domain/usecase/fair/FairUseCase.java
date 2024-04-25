package ecomarkets.core.domain.usecase.fair;

import ecomarkets.core.domain.core.fair.Fair;
import ecomarkets.core.domain.core.fair.FairId;
import ecomarkets.core.domain.core.fair.ShoppingPeriod;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class FairUseCase {

    @Transactional
    public Fair newFair(ShoppingPeriod shoppingPeriod){
        Fair fair = Fair.of(shoppingPeriod);
        fair.persist();
        return fair;
    }

    @Transactional
    public Fair changeFair(FairId fairId, ShoppingPeriod shoppingPeriod){
        Fair fair = Fair.findById(fairId.id());
        fair.changeShoppingPeriod(shoppingPeriod);
        return fair;
    }

    @Transactional
    public void deleteFair(FairId fairId){

        Fair.deleteById(fairId.id());
    }

}

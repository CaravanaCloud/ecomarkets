package ecomarkets.core.domain.usecase.farmer;

import ecomarkets.core.domain.core.farmer.Farmer;
import ecomarkets.core.domain.core.farmer.FarmerId;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class FarmerUseCase {

    @Transactional
    public Farmer newFarmer(Farmer farmer){
        farmer.persist();
        return farmer;
    }

    @Transactional
    public Farmer changeFarmer(Farmer farmer){
        return Farmer.getEntityManager().merge(farmer);
    }

    @Transactional
    public void deleteFarmer(FarmerId farmerId){

        //TODO add rules to verify if farmer can be removed. For example, if there is some basket delivered by the farmer, can not be removed.
        Farmer.deleteById(farmerId.id());
    }

}

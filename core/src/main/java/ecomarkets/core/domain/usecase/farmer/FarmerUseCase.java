package ecomarkets.core.domain.usecase.farmer;

import ecomarkets.core.domain.core.farmer.Farmer;
import ecomarkets.core.domain.core.farmer.FarmerId;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class FarmerUseCase {

    @Transactional
    public Farmer newFarmer(String name){
        return null;
    }

    @Transactional
    public Farmer changeFarmer(Long id, String name){
        return null;
    }

    @Transactional
    public void deleteFarmer(FarmerId farmerId){
    }

}

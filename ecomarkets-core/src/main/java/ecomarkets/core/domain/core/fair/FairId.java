package ecomarkets.core.domain.core.fair;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record FairId (@Column(name = "fair_id") Long id){

    public static FairId of(Long id){
        return new FairId(id);
    }
}

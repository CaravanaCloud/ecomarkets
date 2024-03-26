package ecomarkets.domain.core.farmer;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record FarmerId (@Column(name = "farmer_id", nullable = false) Long id){

    public static FarmerId of(Long id) {
        return new FarmerId(id);
    }

}

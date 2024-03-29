package ecomarkets.domain.core.partner;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record PartnerId (@Column(name = "partner_id", nullable = false) Long id){

    public static PartnerId of(Long id) {
        return new PartnerId(id);
    }

}

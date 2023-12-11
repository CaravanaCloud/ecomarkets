package ecomarkets.domain.core.partner;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class PartnerId {

    @Column(name = "partner_id", nullable = false)
    private Long id;

    private PartnerId() {
    }

    public static PartnerId of(Long id) {
        PartnerId result = new PartnerId();
        result.id = id;
        return result;
    }

    public Long getId() {
        return this.id;
    }

}

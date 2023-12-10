package ecomarkets.domain.core.farmer;

import jakarta.persistence.Column;

public class FarmerId {

    @Column(name = "farmer_id", nullable = false)
    private Long id;

    private FarmerId() {
    }

    public static FarmerId of(Long id) {
        FarmerId result = new FarmerId();
        result.id = id;
        return result;
    }

    public Long getId() {
        return this.id;
    }
}

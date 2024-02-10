package ecomarkets.domain.core.fair;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Embeddable
public record ShoppingPeriod(@NotNull LocalDateTime start, @NotNull LocalDateTime end) {

    public ShoppingPeriod {
        if(!end.isAfter(start)){
            throw new IllegalStateException("end date should be after start date.");
        }
    }

    public static ShoppingPeriod of(LocalDateTime start, LocalDateTime end){
        return new ShoppingPeriod(start, end);
    }
}

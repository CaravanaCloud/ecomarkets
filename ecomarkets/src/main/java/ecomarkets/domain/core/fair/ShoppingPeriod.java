package ecomarkets.domain.core.fair;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Embeddable
public record ShoppingPeriod(@NotNull LocalDateTime startDate, @NotNull LocalDateTime endDate) {

    public ShoppingPeriod {
        if(!endDate.isAfter(startDate)){
            throw new IllegalStateException("endDate date should be after startDate date.");
        }
    }

    public static ShoppingPeriod of(LocalDateTime start, LocalDateTime end){
        return new ShoppingPeriod(start, end);
    }
}

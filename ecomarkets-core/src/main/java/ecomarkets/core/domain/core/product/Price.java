package ecomarkets.core.domain.core.product;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public record Price(@Column(name = "price_unit") Integer unit, Integer cents) {

    public Price{
        if(unit < 0){
            throw new IllegalArgumentException("value should be greater then zero!");
        }
        if(cents < 0 || cents > 99){
            throw new IllegalArgumentException("cents should be between 0 and 99!");
        }
    }

    public BigDecimal total(){
        return BigDecimal.valueOf(unit()).add(BigDecimal.valueOf(cents()).divide(BigDecimal.valueOf(100.0)));
    }

    public static Price of(Integer unit, Integer cents){
        return new Price(unit, cents);
    }
}

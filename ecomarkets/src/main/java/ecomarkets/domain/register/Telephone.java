package ecomarkets.domain.register;

import jakarta.persistence.Embeddable;

@Embeddable
public record Telephone (Integer areaCode, Integer number){

    public static Telephone of(Integer areaCode, Integer number){
        return new Telephone(areaCode, number);
    }

}

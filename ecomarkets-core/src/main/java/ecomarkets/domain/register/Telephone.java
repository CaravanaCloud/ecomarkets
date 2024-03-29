package ecomarkets.domain.register;

import jakarta.persistence.Embeddable;

@Embeddable
public record Telephone (String areaCode, String number){

    public static Telephone of(String areaCode, String number){
        return new Telephone(areaCode, number);
    }

}

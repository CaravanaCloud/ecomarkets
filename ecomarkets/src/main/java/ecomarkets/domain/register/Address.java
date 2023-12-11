package ecomarkets.domain.register;

import jakarta.persistence.Embeddable;

@Embeddable
public record Address (String country, 
                       String state,
                       String city,
                       Integer houseNumber,
                       String addOn,
                       String reference,
                       Integer postCode){

    public static Address of(String country, 
    String state,
    String city,
    Integer houseNumber,
    String addOn,
     String reference,
     Integer postCode){
        Address address = new Address(country,
        state, 
        city,
        houseNumber,
        addOn,
        reference,
        postCode);
        return address;
    }
}
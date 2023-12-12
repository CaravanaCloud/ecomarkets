package ecomarkets.domain.register;

import jakarta.persistence.Embeddable;

@Embeddable
public record Email (String email){
    
    public static Email of(String email){
        return new Email(email);
    }

}

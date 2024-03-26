package ecomarkets.domain.register;

import jakarta.persistence.Embeddable;

@Embeddable
public record EmailAddress(String value){
    
    public static EmailAddress of(String email){
        return new EmailAddress(email);
    }

}

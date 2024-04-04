package ecomarkets.core.domain.register;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record EmailAddress(@Column(name = "email") String value){
    
    public static EmailAddress of(String email){
        return new EmailAddress(email);
    }

}

package ecomarkets.domain.register;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Email implements Serializable{

    @Column(name = "email")
    private String value;

    public Email(String value){
        this.value = value; 
    }

    public String getValue() {
        return this.value;
    }

}

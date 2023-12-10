package ecomarkets.domain.register;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Email implements Serializable{

    @Column(name = "email")
    private String value;

    private Email(){}

    public static Email of(String value){
        Email e = new Email();
        e.value = value;
        return e;
    }

    public String getValue() {
        return this.value;
    }

}

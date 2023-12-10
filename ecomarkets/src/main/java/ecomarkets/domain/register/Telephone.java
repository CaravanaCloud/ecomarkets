package ecomarkets.domain.register;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Telephone implements Serializable{

    @Column(name = "telephone")
    private Integer number;

    private Telephone(){}

    public static Telephone of(Integer number){
        Telephone tel = new Telephone();
        tel.number = number;
        return tel;
    }

    public Integer getNumber() {
        return this.number;
    }

}

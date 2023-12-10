package ecomarkets.domain.register;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Telephone implements Serializable{

    @Column(name = "telephone")
    private Integer number;

    public Telephone(Integer number){
        this.number = number;
    }


    public Integer getNumber() {
        return this.number;
    }

}

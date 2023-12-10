package ecomarkets.domain.register;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class CPF implements Serializable{

    @Column(name = "cpf")
    Integer value;

    public CPF(Integer value){
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }

}

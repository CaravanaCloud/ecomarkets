package ecomarkets.domain.register;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class CPF implements Serializable{

    @Column(name = "cpf")
    Integer value;

    private CPF(){}

    public static CPF of(Integer value){
        CPF cpf = new CPF();
        cpf.value = value;
        return cpf;
    }

    public Integer getValue() {
        return this.value;
    }

}

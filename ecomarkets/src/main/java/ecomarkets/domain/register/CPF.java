package ecomarkets.domain.register;

import jakarta.persistence.Embeddable;

@Embeddable
public record CPF (Integer cpf){

    public static CPF of(Integer cpf){
        return new CPF(cpf);
    }
    
}

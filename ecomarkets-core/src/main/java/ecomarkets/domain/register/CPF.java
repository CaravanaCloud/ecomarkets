package ecomarkets.domain.register;

import jakarta.persistence.Embeddable;

@Embeddable
public record CPF (String cpf){

    public static CPF of(String cpf){
        return new CPF(cpf);
    }
    
}

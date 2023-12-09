package ecomarkets.domain.core;

import java.time.LocalDate;

import ecomarkets.domain.register.Address;
import ecomarkets.domain.register.CPF;
import ecomarkets.domain.register.Email;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class Partner extends PanacheEntity{

    public String name;

    public CPF cpf;
    
    public Email email;

    public LocalDate birthDate;

    @ManyToOne
    public Address address;

}


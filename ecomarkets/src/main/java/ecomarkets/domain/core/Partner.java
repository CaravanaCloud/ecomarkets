package ecomarkets.domain.core;

import java.time.LocalDate;

import ecomarkets.domain.register.Address;
import ecomarkets.domain.register.CPF;
import ecomarkets.domain.register.Email;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class Partner extends PanacheEntity{

    private String name;

    private CPF cpf;
    
    private Email email;

    private LocalDate birthDate;

    @ManyToOne(cascade = CascadeType.ALL)
    public Address address;

    private Partner() {}

    private Partner(String name, CPF cpf, Email email, LocalDate birthDate, Address address) {
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.birthDate = birthDate;
        this.address = address;
    }

    public static Partner of(String name,
    CPF cpf,
    Email email,
    LocalDate birthDate,
    Address address
    ){
        return new Partner(
            name,
            cpf,
            email,
            birthDate,
            address
        );
    }

    public String getName() {
        return this.name;
    }

    public CPF getCpf() {
        return this.cpf;
    }

    public Email getEmail() {
        return this.email;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public Address getAddress() {
        return this.address;
    }

}


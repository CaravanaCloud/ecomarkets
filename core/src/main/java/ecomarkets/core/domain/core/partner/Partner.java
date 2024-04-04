package ecomarkets.core.domain.core.partner;

import java.time.LocalDate;

import ecomarkets.core.domain.register.Address;
import ecomarkets.core.domain.register.CPF;
import ecomarkets.core.domain.register.EmailAddress;
import ecomarkets.core.domain.register.Telephone;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
/*
  Partner is a generic name for "Customer"
 */
public class Partner extends PanacheEntity{

    private String name;

    private CPF cpf;
    
    private EmailAddress emailAddress;
    
    private LocalDate birthDate;
    
    private Telephone telephone;

    private Address address;

    private Partner() {}

    private Partner(String name, CPF cpf, EmailAddress emailAddress, LocalDate birthDate, Address address, Telephone telephone) {
        this.name = name;
        this.cpf = cpf;
        this.emailAddress = emailAddress;
        this.birthDate = birthDate;
        this.address = address;
        this.telephone = telephone;
    }

    public static Partner of(String name,
    CPF cpf,
    EmailAddress emailAddress,
    LocalDate birthDate,
    Telephone telephone,
    Address address
    ){
        return new Partner(
            name,
            cpf,
                emailAddress,
            birthDate,
            address,
            telephone
        );
    }

    public String getName() {
        return this.name;
    }

    public CPF getCpf() {
        return this.cpf;
    }

    public EmailAddress getEmailAddress() {
        return this.emailAddress;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public Address getAddress() {
        return this.address;
    }

    public Telephone getTelephone() {
        return this.telephone;
    }

    public PartnerId partnerId(){
        return PartnerId.of(id);
    }

}


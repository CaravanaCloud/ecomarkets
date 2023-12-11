package ecomarkets.domain.core.partner;

import java.time.LocalDate;

import ecomarkets.domain.register.Address;
import ecomarkets.domain.register.CPF;
import ecomarkets.domain.register.Email;
import ecomarkets.domain.register.Telephone;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;

@Entity
/**
 * Partner is a generic name for "Customer"
 */
public class Partner extends PanacheEntity{

    private String name;

    private CPF cpf;
    
    private Email email;

    private LocalDate birthDate;

    private Telephone telephone;

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    private Partner() {}

    private Partner(String name, CPF cpf, Email email, LocalDate birthDate, Address address, Telephone telephone) {
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.birthDate = birthDate;
        this.address = address;
        this.telephone = telephone;
    }

    public static Partner of(String name,
    CPF cpf,
    Email email,
    LocalDate birthDate,
    Telephone telephone,
    Address address
    ){
        return new Partner(
            name,
            cpf,
            email,
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

    public Email getEmail() {
        return this.email;
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


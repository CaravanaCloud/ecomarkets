package ecomarkets.domain.core.farmer;

import ecomarkets.domain.register.Address;
import ecomarkets.domain.register.Email;
import ecomarkets.domain.register.Telephone;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Farmer extends PanacheEntity{

    private String name;

    private Email email;

    private Telephone telephone;

    private Address address;
    
    private Farmer() {}

    private Farmer(String name, Email email, Address address, Telephone telephone) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.telephone = telephone;
    }

    public static Farmer of(String name,
    Email email,
    Telephone telephone,
    Address address
    ){
        return new Farmer(
            name,
            email,
            address,
            telephone
        );
    }

    public String getName() {
        return this.name;
    }

    public Email getEmail() {
        return this.email;
    }

    public Address getAddress() {
        return this.address;
    }

    public Telephone getTelephone() {
        return this.telephone;
    }

    public FarmerId farmerId(){
        return FarmerId.of(id);
    }

}

package ecomarkets.core.domain.core.farmer;

import ecomarkets.core.domain.register.Address;
import ecomarkets.core.domain.register.EmailAddress;
import ecomarkets.core.domain.register.Telephone;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Farmer extends PanacheEntity{

    private String name;

    private EmailAddress emailAddress;

    private Telephone telephone;

    private Address address;
    
    private Farmer() {}

    private Farmer(String name, EmailAddress emailAddress, Address address, Telephone telephone) {
        this.name = name;
        this.emailAddress = emailAddress;
        this.address = address;
        this.telephone = telephone;
    }

    public static Farmer of(String name,
    EmailAddress emailAddress,
    Telephone telephone,
    Address address
    ){
        return new Farmer(
            name,
                emailAddress,
            address,
            telephone
        );
    }

    public String getName() {
        return this.name;
    }

    public EmailAddress getEmail() {
        return this.emailAddress;
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

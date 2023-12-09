package ecomarkets.domain.register;

import org.hibernate.annotations.Immutable;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
@Immutable
public class Address extends PanacheEntity{

    private String country;
    
    private String state;
    
    private String city;
    
    private Integer houseNumber;
    
    private String addOn;
    
    private String reference;

    private Integer postCode;

    public static Address of(String country, 
    String state,
    String city,
    Integer houseNumber,
    String addOn,
     String reference,
     Integer postCode){
        Address address = new Address();
        address.state = state;
        address.city = city;
        address.houseNumber = houseNumber;
        address.addOn = addOn;
        address.reference = reference;
        address.postCode = postCode;
        return address;
    }

    public String country(){
        return this.country;
    }
    
    public String state(){
        return this.state;
    }
    
    public String city(){
        return this.city;
    }

    public Integer houseNumber(){
        return this.houseNumber;
    }

    public String addOn(){
        return this.addOn;
    }

    public String reference(){
        return this.reference;
    }
  
    public Integer postCode(){
        return this.postCode;
    }


}
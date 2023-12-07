package ecomarkets.core.entity;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
@Immutable
public class Address {

    @Id
    @GeneratedValue
    private Long id;

    private String country;
    
    private String state;
    
    private String city;
    
    private Integer houseNumber;
    
    private String addOn;
    
    private String reference;

    private Integer postCode;

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
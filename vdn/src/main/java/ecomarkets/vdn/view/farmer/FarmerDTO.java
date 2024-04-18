package ecomarkets.vdn.view.farmer;

import ecomarkets.core.domain.core.farmer.Farmer;
import ecomarkets.core.domain.register.Address;
import ecomarkets.core.domain.register.EmailAddress;
import ecomarkets.core.domain.register.Telephone;

public class FarmerDTO {

    private Long id;
    private String name;
    private String email;
    private String telephone;
    private String country;
    private String state;
    private String city;
    private Integer houseNumber;
    private String addOn;
    private String reference;
    private Integer postCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(Integer houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getAddOn() {
        return addOn;
    }

    public void setAddOn(String addOn) {
        this.addOn = addOn;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Integer getPostCode() {
        return postCode;
    }

    public void setPostCode(Integer postCode) {
        this.postCode = postCode;
    }

    public Farmer parseFarmer(){
        Telephone tel = new Telephone("", telephone);
        EmailAddress emailAddress = EmailAddress.of(email);
        Address address = Address.of(this.country,
                this.state,
                this.city,
                this.houseNumber,
                this.addOn,
                this.reference,
                this.postCode);
        Farmer farmer = Farmer.of(this.name, emailAddress, tel, address);
        farmer.id = this.id;
        return farmer;
    }
}

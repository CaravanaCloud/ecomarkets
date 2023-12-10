package ecomarkets.domain.register;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Telephone implements Serializable{

    @Column(name = "phone_area_code")
    private Integer areaCode;

    @Column(name = "phone_number")
    private Integer number;

    private Telephone(){}

    public static Telephone of(Integer areaCode, Integer number){
        Telephone tel = new Telephone();
        tel.number = number;
        return tel;
    }

    public Integer getNumber() {
        return this.number;
    }

    public Integer getAreaCode() {
        return this.areaCode;
    }
    
}

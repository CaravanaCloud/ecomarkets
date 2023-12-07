package ecomarkets.core.entity;


import java.util.Collection;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Basket extends PanacheEntityBase {
 
    @Id
    @GeneratedValue
    Long id;

    //@ManyToOne
    //Tenant tenant;

    Collection<Long> products;

    

}

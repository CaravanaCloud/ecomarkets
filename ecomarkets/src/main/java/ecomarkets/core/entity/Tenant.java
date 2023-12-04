package ecomarkets.core.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;

@Entity
@NamedQuery(name = "tenant.byCode", query = "from Tenant where code = :code")
public class Tenant extends PanacheEntityBase {
    @Id
    @GeneratedValue
    Long id;

    String code;

    String name;

    public Tenant(){}

    public static final Tenant of(String name, String code){
        var tenant = new Tenant();
        tenant.name = name;
        tenant.code = code;
        return tenant;
    }

    public Long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
}

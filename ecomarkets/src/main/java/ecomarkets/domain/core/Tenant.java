package ecomarkets.domain.core;

import com.google.errorprone.annotations.Immutable;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
@Immutable
public class Tenant extends PanacheEntity {
    
    private String code;

    private String name;

    public Tenant(){}

    public static final Tenant of(String name, String code){
        var tenant = new Tenant();
        tenant.name = name;
        tenant.code = code;
        return tenant;
    }

    public String name() {
        return name;
    }
    
    public String code() {
        return code;
    }


}

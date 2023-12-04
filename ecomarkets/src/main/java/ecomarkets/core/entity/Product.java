package ecomarkets.core.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;

@Entity
@NamedQuery(name = "product.byTenantCode", 
    query = "from Product where tenant.code = :tenantCode")
public class Product extends PanacheEntityBase {

    @Id
    @GeneratedValue
    Long id;

    @ManyToOne
    Tenant tenant;

    String name;

    public Product(){}

    public static final Product of(Tenant tenant, String name){
        var product = new Product();
        product.name = name;
        product.tenant = tenant;
        return product;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }
    
}

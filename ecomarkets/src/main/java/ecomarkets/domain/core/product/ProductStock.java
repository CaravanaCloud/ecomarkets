package ecomarkets.domain.core.product;

import com.google.errorprone.annotations.Immutable;

import ecomarkets.domain.core.Tenant;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
@Immutable
public class ProductStock extends PanacheEntity{

    @ManyToOne
    private Tenant tenant;

    @ManyToOne
    private Product product;

    private Double amount;

    ProductStock(){}

    public ProductStock(Tenant tenant,
    Product product,
    Double amount){
        this.tenant = tenant;
        this.product = product;
    }

    public Tenant getTenant(){
        return this.tenant;
    }
    
    public Product getProduct(){
        return this.product;
    }

    public Double getAmount(){
        return this.amount;
    }
    
    public void defineTenant(Tenant tenant){
        if(this.id != null){
            throw new IllegalArgumentException("Stock already created!");
        }
        this.tenant = tenant;
    }
    
}

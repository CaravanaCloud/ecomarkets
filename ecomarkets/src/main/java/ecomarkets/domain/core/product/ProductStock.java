package ecomarkets.domain.core.product;

import com.google.errorprone.annotations.Immutable;

import ecomarkets.domain.core.Tenant;
import ecomarkets.domain.core.farmer.FarmerId;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
@Immutable
public class ProductStock extends PanacheEntity{

    private FarmerId farmerId;

    @ManyToOne
    private Tenant tenant;

    @OneToOne
    private Product product;

    private Integer amount;

    private ProductStock(){}

    public static ProductStock of(Tenant tenant,
    FarmerId farmerId,
    Product product,
    Integer amount){
        ProductStock result = new ProductStock();
        result.tenant = tenant;
        result.farmerId = farmerId;
        result.product = product;
        result.amount = amount;
        return result;
    }

    public Tenant getTenant(){
        return this.tenant;
    }
    
    public Product getProduct(){
        return this.product;
    }

    public Integer getAmount(){
        return this.amount;
    }
    
    public FarmerId getFarmerId(){
        return this.farmerId;
    }
    
}

package ecomarkets.domain.core.basket;

import java.time.LocalDateTime;

import com.google.errorprone.annotations.Immutable;

import ecomarkets.domain.core.product.Product;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
@Immutable
public class BasketItem extends PanacheEntity{

    @ManyToOne
    private Product product;

    private Double amount;

    private LocalDateTime creationDate;
    
    private BasketItem(){}

    public static BasketItem of(Product product,
    Double amount,
    LocalDateTime creationDate){
        BasketItem bi = new BasketItem();
        bi.product = product;
        bi.amount = amount;
        bi.creationDate = creationDate;
        return bi;
    }

    public BasketItem(Product product,
    Double amount,
    LocalDateTime creationDate){
        this.product = product;
        this.amount = amount;
        this.creationDate = creationDate;
    }

    public Product getProduct(){
        return this.product;
    }
    
    public Double getAmount(){
        return this.amount;
    }
 
    public LocalDateTime getCreationDate(){
        return this.creationDate;
    }

}

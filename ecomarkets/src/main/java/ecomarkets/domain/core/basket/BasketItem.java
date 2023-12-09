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
    
    public BasketItem(){
    }

    public BasketItem(Product product,
    Double amount,
    LocalDateTime creationDate){
        this.product = product;
        this.amount = amount;
        this.creationDate = creationDate;
    }

    public Product product(){
        return this.product;
    }
    
    public Double amount(){
        return this.amount;
    }
 
    public LocalDateTime creationDate(){
        return this.creationDate;
    }

}

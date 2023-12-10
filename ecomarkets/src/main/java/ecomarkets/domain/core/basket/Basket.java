package ecomarkets.domain.core.basket;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import ecomarkets.domain.core.Tenant;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Basket extends PanacheEntity {
 
    @ManyToOne
    private Tenant tenant;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<BasketItem> items;

    private LocalDateTime creationDate;
    
    private LocalDateTime reservedDate;
    
    private LocalDateTime deliveredDate;

    public Basket(){
        this.creationDate = LocalDateTime.now();
    }

    public void getReserveBasket(){
        this.reservedDate = LocalDateTime.now();
    }
    
    public void getDeliverBasket(){
        this.deliveredDate = LocalDateTime.now();
    }

    public LocalDateTime getCreationDate(){
        return this.creationDate;
    }
    
    public LocalDateTime getReservedDate(){
        return this.reservedDate;
    }
    
    public LocalDateTime getDeliveredDate(){
        return this.deliveredDate;
    }

    public Tenant getTenant(){
        return this.tenant;
    }

    public Collection<BasketItem> getItems(){
        return new ArrayList<>(this.items);
    }

    public void addItem(BasketItem item){
        if(this.items == null){
            throw new IllegalArgumentException("product is null");
        }
        if(item.id != null){
            throw new IllegalArgumentException("Item already inside a Basket!");
        }

        this.items.add(item);
    }



}

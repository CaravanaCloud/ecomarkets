package ecomarkets.domain.core.basket;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import ecomarkets.domain.core.Tenant;
import ecomarkets.domain.core.partner.PartnerId;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;

@Entity
public class Basket extends PanacheEntity {
 
    private PartnerId partnerId;

    @OneToOne
    private Tenant tenant;
    
    private LocalDateTime creationDate;
    
    private LocalDateTime reservedDate;
    
    private LocalDateTime deliveredDate;
    
    @ElementCollection
    private Collection<BasketItem> items;

    private Basket(){}
        
    public static Basket of(Tenant tenant, PartnerId partnerId){
        Basket result = new Basket();
        result.creationDate = LocalDateTime.now();
        result.tenant = tenant;
        result.partnerId = partnerId;
        result.items = new ArrayList<>();
        return result;
    }

    public void reserveBasket(){
        this.reservedDate = LocalDateTime.now();
    }
    
    public void deliverBasket(){
        this.deliveredDate = LocalDateTime.now();
    }

    public LocalDateTime getCreationDate(){
        return this.creationDate;
    }
    
    public Optional<LocalDateTime> getReservedDate(){
        return Optional.ofNullable(this.reservedDate);
    }
    
    public Optional<LocalDateTime> getDeliveredDate(){
        return Optional.ofNullable(this.deliveredDate);
    }
    
    public PartnerId getPartnerId(){
        return this.partnerId;
    }

    public Tenant getTenant(){
        return this.tenant;
    }

    public Collection<BasketItem> getItems(){
        return new ArrayList<>(this.items);
    }

    public void addItem(BasketItem item){
        if(this.reservedDate != null){
            throw new IllegalStateException("Basket Already scheduled to delivery.");
        }

        if(this.items == null){
            throw new IllegalArgumentException("product is null");
        }

        this.items.add(item);
    }

    public void addItems(Collection<BasketItem> items){
        if(items == null)
            return;

        items.forEach(this::addItem);
    }


}

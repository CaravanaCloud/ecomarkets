package ecomarkets.domain.core.basket;

import com.google.errorprone.annotations.Immutable;
import ecomarkets.domain.core.basket.event.BasketDeliveredEvent;
import ecomarkets.domain.core.basket.event.BasketReservedEvent;
import ecomarkets.domain.core.fair.FairId;
import ecomarkets.domain.core.partner.PartnerId;
import ecomarkets.domain.core.product.Price;
import ecomarkets.domain.core.product.Product;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Immutable
public class Basket extends PanacheEntity {

    private FairId fairId;

    private PartnerId partnerId;

    private LocalDateTime creationDate;
    
    @ElementCollection
    private Collection<BasketItem> items;

    private Basket(){}
        
    public static Basket of(FairId fairId, PartnerId partnerId){
        Basket result = new Basket();
        result.creationDate = LocalDateTime.now();
        result.partnerId = partnerId;
        result.items = new ArrayList<>();
        result.fairId = fairId;
        return result;
    }

    public boolean isReserved(){
        return BasketReservedEvent.count("basketId", basketId()) > 0;
    }

    public boolean isDelivered(){
        return BasketDeliveredEvent.count("basketId", basketId()) > 0;
    }

    public BasketReservedEvent reserveBasket(){
        if(this.id == null){
            throw new IllegalStateException("Basket not created yet!");
        }

        if(this.items == null || this.items.isEmpty()){
            throw new IllegalStateException("There are no items added to the Basket!");
        }

        if(isReserved()){
            throw new IllegalStateException("Basket already reserved!");
        }

        return new BasketReservedEvent(basketId());
    }

    public BasketDeliveredEvent deliverBasket(){
        if(isDelivered()){
            throw new IllegalStateException("Basket already delivered!");
        }
        return new BasketDeliveredEvent(this.basketId());
    }

    public LocalDateTime getCreationDate(){
        return this.creationDate;
    }
    
    public PartnerId getPartnerId(){
        return this.partnerId;
    }

    public Collection<BasketItem> getItems(){
        return new ArrayList<>(this.items);
    }

    public void addItem(Product product,
                        Integer amount){
        if(BasketReservedEvent.count("basketId", basketId()) > 0){
            throw new IllegalStateException("Basket Already scheduled to delivery.");
        }
        if(this.items == null){
            throw new IllegalArgumentException("product is null");
        }

        this.items.add(BasketItem.of(product.productId(), amount, totalPayment(product.getPrice(), amount)));
    }

    //TODO WIP - it is necessary refactor for a better solution
    private Double totalPayment(Price price, Integer amount){
        return (price.unit() + price.cents() / 100.0) * amount;
    }

    public Double totalPayment(){
        return this.items.stream().mapToDouble(BasketItem::totalPayment).sum();
    }

    public BasketId basketId(){
        return BasketId.of(this.id);
    }

    public FairId getFairId() {
        return fairId;
    }
}

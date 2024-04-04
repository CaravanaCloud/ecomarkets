package ecomarkets.core.domain.core.basket;

import com.google.errorprone.annotations.Immutable;

import ecomarkets.core.domain.core.basket.event.BasketDeliveredEvent;
import ecomarkets.core.domain.core.basket.event.BasketReservedEvent;
import ecomarkets.core.domain.core.fair.FairId;
import ecomarkets.core.domain.core.fair.ProductStock;
import ecomarkets.core.domain.core.partner.PartnerId;
import ecomarkets.core.domain.core.product.Price;
import ecomarkets.core.domain.core.product.Product;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;

import java.math.BigDecimal;
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

        BasketReservedEvent event = new BasketReservedEvent(basketId());
        event.persist();
        return event;
    }

    public BasketDeliveredEvent deliverBasket(){
        if(isDelivered()){
            throw new IllegalStateException("Basket already delivered!");
        }
        BasketDeliveredEvent basketDeliveredEvent = new BasketDeliveredEvent(basketId());
        basketDeliveredEvent.persist();
        return basketDeliveredEvent;
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

    public void addItem(
            ProductStock productStock,
            Product product,
            Integer amount){
        if(BasketReservedEvent.count("basketId", basketId()) > 0){
            throw new IllegalStateException("Basket Already scheduled to delivery.");
        }
        if(this.items == null){
            throw new IllegalArgumentException("product is null");
        }

        Double amountAvailable = productStock.getAmountProductAvailable(fairId, product.productId());

        if(amountAvailable < amount){
            throw new IllegalArgumentException("There isn't product amount available!");
        }

        this.items.add(BasketItem.of(product.productId(), amount, totalPayment(product.getPrice(), amount)));
    }

    //TODO WIP - it is necessary refactor for a better solution
    private BigDecimal totalPayment(Price price, Integer amount){
        return price.total().multiply(new BigDecimal(amount));
    }

    public BigDecimal totalPayment(){
        return this.items.stream().map(BasketItem::totalPayment).reduce((a1, a2) -> a1.add(a2)).orElse(BigDecimal.ZERO);
    }

    public BasketId basketId(){
        return BasketId.of(this.id);
    }

    public FairId getFairId() {
        return fairId;
    }
}

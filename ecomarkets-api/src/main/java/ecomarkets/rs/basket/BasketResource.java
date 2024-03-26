package ecomarkets.rs.basket;

import ecomarkets.domain.core.basket.Basket;
import ecomarkets.domain.core.basket.event.BasketEvent;
import ecomarkets.domain.core.fair.ProductStock;
import ecomarkets.domain.core.product.Product;
import ecomarkets.rs.basket.form.BasketItemForm;
import ecomarkets.rs.basket.form.CreateBasketForm;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Collection;
import java.util.List;

@Path("/basket")
public class BasketResource {

    @Inject
    Event<BasketEvent> basketEvent;

    @Inject
    ProductStock productStock;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Basket> getBaskets() {
        return Basket.listAll(Sort.descending("reservedDate"));
    }
    
    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Basket getBasket(@PathParam("id") Long id) {
        return Basket.findById(id);
    }
    
    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBasket(CreateBasketForm createBasketForm) {
        
        Basket basket = Basket.of(createBasketForm.fairId(), createBasketForm.partnerId());
        if(createBasketForm.items() != null){
            createBasketForm.items().forEach(it -> basket.addItem(productStock, Product.findById(it.productId().id()), it.amount()));
        }
        basket.persist();

        return Response
        .status(Response.Status.CREATED)
        .entity(basket)
        .build();
    }
    
    @Path("/{id}/item")
    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addBasketItem(@PathParam("id") Integer id,
    Collection<BasketItemForm> items) {
        
        Basket basket = Basket.findById(id);

        if(basket == null){
            throw new NotFoundException("Basket do not exists for id " + id);
        }
        
        items.forEach(it -> basket.addItem(productStock, Product.findById(it.productId().id()), it.amount()));
        
        return Response
        .status(Response.Status.CREATED)
        .entity(basket)
        .build();
    }

    @Path("/{id}/reserve")
    @PUT
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Basket reserveBasket(@PathParam("id") Integer id) {
        
        Basket basket = Basket.findById(id);

        if(basket == null){
            throw new NotFoundException("Basket do not exists for id " + id);
        }

        BasketEvent event = basket.reserveBasket();
        basketEvent.fire(event);

        return basket;
    }

    @Path("/{id}/deliver")
    @PUT
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Basket deliverBasket(@PathParam("id") Integer id) {
        
        Basket basket = Basket.findById(id);

        if(basket == null){
            throw new NotFoundException("Basket do not exists for id " + id);
        }

        BasketEvent event = basket.deliverBasket();
        basketEvent.fire(event);

        return basket;
    }

}

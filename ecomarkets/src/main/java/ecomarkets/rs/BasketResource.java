package ecomarkets.rs;

import java.util.Collection;
import java.util.List;

import ecomarkets.domain.core.Tenant;
import ecomarkets.domain.core.basket.Basket;
import ecomarkets.domain.core.basket.BasketItem;
import ecomarkets.domain.core.partner.PartnerId;
import io.quarkus.panache.common.Sort;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/basket")
public class BasketResource {

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
    
    @Path("/{tenantCode}/{partnerId}")
    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBasket(@PathParam("tenantCode") Integer tenantCode,
    @PathParam("partnerId") Long partnerId,
    Collection<BasketItem> items) {
        
        Tenant tenant = Tenant.find("code", tenantCode).firstResult();

        Basket basket = Basket.of(tenant, PartnerId.of(partnerId));
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
    Collection<BasketItem> items) {
        
        Basket basket = Basket.findById(id);

        if(basket == null){
            throw new NotFoundException("Basket do not exists for id " + id);
        }
        
        basket.addItems(items);
        
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

        basket.reserveBasket();
        
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

        basket.deliverBasket();
        
        return basket;
    }

}

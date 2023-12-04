package ecomarkets.rs;

import java.util.List;

import ecomarkets.core.entity.Product;
import ecomarkets.core.entity.Tenant;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/{tenantCode}/products")
public class ProductResource {

    @Transactional
    public void init(@Observes StartupEvent event) {
        System.out.println("Initializing test database...");
        var tenant = Tenant.of("Farmer's Market", "ecomkt");
        tenant.persist();
        Product.of(tenant, "Apples").persist();
        Product.of(tenant, "Oranges").persist();
        Product.of(tenant, "Bananas").persist();
    }

    @Inject
    EntityManager em;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> getProducts(@PathParam ("tenantCode") String tenantCode) {
        List<Product> result = em.createNamedQuery("product.byTenantCode", Product.class)
            .setParameter("tenantCode", tenantCode)
            .getResultList();
        return result;
    }
    
    // curl -X POST -H "Content-Type: application/json" -d '{"name":"abacate"}' http://localhost:9090/api/ecomkt/products
    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Product postProduct(@PathParam ("tenantCode") String tenantCode,
        Product product) {
        var tenant = em.createNamedQuery("tenant.byCode", Tenant.class)
            .setParameter("code", tenantCode)
            .getSingleResult();
        product.setTenant(tenant);
        product.persist();
        return product;
    }

}

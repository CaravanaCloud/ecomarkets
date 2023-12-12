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
import jakarta.inject.Inject;
import java.util.Map;
import javax.sql.DataSource;

@Path("/_hc")
public class HealthCheckResource {
    @Inject
    DataSource ds;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> getHealthCheck() {
        var result = Map.of("datasrouce.isValid", isValidDatasource());
        return result;
    }

    public String isValidDatasource(){
        try(var conn = ds.getConnection()){
            return "" + conn.isValid(30);
        }catch (Exception e){
            return e.getMessage();
        }
    }

}

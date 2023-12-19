package ecomarkets.rs;

import java.util.Map;

import javax.sql.DataSource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

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

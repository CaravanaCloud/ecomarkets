package ecomarkets.infra;

import ecomarkets.core.infra.HealthCheckService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Map;

@Path("/_hc")
public class HealthCheckResource {

    @Inject
    HealthCheckService healthCheckService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> getHealthCheck() {
        return healthCheckService.getHealthCheck();
    }

}

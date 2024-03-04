package ecomarkets.infra;

import java.util.Map;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/user/_whoami")
public class WhoAmIResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> get() {
        var username = "???";
        var result = Map.of(
            "username", username
        );
        return result;
    }
}

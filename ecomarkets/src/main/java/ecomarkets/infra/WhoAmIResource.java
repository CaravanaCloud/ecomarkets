package ecomarkets.infra;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

import io.quarkus.oidc.IdToken;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/user/_whoami")
public class WhoAmIResource {
    @Inject
    @IdToken
   JsonWebToken idToken;
 
   @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> get() {
        var result  = idToken.getClaimNames().stream()
            .collect(Collectors.toMap(
                name -> name,
                name -> idToken.getClaim(name).toString()
            ));
        return result;
    }
}

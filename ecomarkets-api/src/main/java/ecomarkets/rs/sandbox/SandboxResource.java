package ecomarkets.rs.sandbox;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.twilio.*;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;

@Path("_sandbox")
public class SandboxResource {

    @ConfigProperty(name = "twilio.phone_from") 
    String twilioPhoneFrom;
    
    @GET
    @Produces("text/plain")
    public String get() {
        return "Hello, Sandbox!";
    }

    
    @GET
    @Path("send")
    @Produces("text/plain")
    public String send(@QueryParam("to") @DefaultValue("+34663867989") String to, @QueryParam("msg") String text) {
        var allowed = Set.of("+34663867989");
        if (! allowed.contains(to)){
            return "Not allowed";
        }
        var message = Message
                .creator(
                        new PhoneNumber(to),
                        new PhoneNumber(twilioPhoneFrom),
                        text + " - " + LocalDateTime.now())
                .create();

        System.out.println(message.getSid());
        return message.getSid();
    }

}

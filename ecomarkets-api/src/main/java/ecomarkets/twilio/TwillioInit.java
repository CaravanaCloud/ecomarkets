package ecomarkets.twilio;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.twilio.Twilio;

import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class TwillioInit {
    @ConfigProperty(name = "twilio.account_sid") 
    String twilioAccountSID;

    @ConfigProperty(name = "twilio.token") 
    String twilioToken;
    
    public void init(@Observes StartupEvent evt) {
        Log.info("Initializing Twilio");
        Twilio.init(twilioAccountSID, twilioToken);
    }
}

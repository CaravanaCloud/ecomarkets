package ecomarkets.rs.webhook;

import java.util.HashMap;
import java.util.concurrent.Future;

import io.quarkus.logging.Log;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import io.vertx.core.http.HttpServerRequest;

@Path("whatsapp-webhook")
public class WhatsappMediaWebhook {
    @GET
    @Produces("application/json")
    public HashMap<String, String> receiveGet(@Context HttpServerRequest  req){
        return receivePost(req);
    }

    @POST
    @Produces("application/json")
    public HashMap<String, String> receivePost(@Context HttpServerRequest  req){
        Log.info("Received request");
        var out = new HashMap<String, String>();
        var body = (""+req.body().result()).toString();
        out.put("timestamp", String.valueOf(System.currentTimeMillis()));
        out.put("body", body);
        req.handler(buffer -> {
            Log.info("Received body: " + buffer.toString());
        });
        return out;
    }
}

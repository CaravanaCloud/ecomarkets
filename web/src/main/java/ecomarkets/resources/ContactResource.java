package ecomarkets.resources;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/contact")
public class ContactResource {
    @Inject
    Template contact;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get() {
        return contact.instance();
    }
}

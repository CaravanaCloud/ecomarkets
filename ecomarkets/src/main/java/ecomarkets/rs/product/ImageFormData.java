package ecomarkets.rs.product;

import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;

import java.io.File;

public class ImageFormData {
    @RestForm
    public File file;

    @RestForm
    @PartType(MediaType.TEXT_PLAIN)
    public String fileName;

    @RestForm
    @PartType(MediaType.TEXT_PLAIN)
    public String mimeType;

}

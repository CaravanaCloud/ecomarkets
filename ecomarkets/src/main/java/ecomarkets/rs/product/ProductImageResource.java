package ecomarkets.rs.product;

import ecomarkets.domain.core.product.Product;
import ecomarkets.domain.core.product.image.ImageRepository;
import ecomarkets.domain.core.product.image.ProductImage;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.apache.http.HttpStatus;
import org.jboss.resteasy.reactive.ResponseStatus;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

@Path("/product/{id}/image")
public class ProductImageResource {

    @Inject
    private ImageRepository imageRepository;

    @PUT
    @ResponseStatus(HttpStatus.SC_OK)
    @Transactional
    public void saveImage(@PathParam("id") Long productId,
                          @RestForm("file") FileUpload file){
        Product product = Product.findById(productId);
        ProductImage pm = product.newImage(imageRepository.getBucketName());
        imageRepository.save(file.uploadedFile(), pm);
    }

    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] getImage(@PathParam("id") Long productId){
        Product product = Product.findById(productId);
        ProductImage pm = product.newImage(imageRepository.getBucketName());
        return imageRepository.find(pm);
    }
}

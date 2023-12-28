package ecomarkets.rs.product;

import ecomarkets.domain.core.product.image.ImageRepository;
import ecomarkets.domain.core.product.Product;
import ecomarkets.domain.core.product.image.ProductImage;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
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
}

package ecomarkets.rs.product;

import ecomarkets.domain.core.product.Product;
import jakarta.inject.Inject;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Path("/product/{id}/image")
public class ProductImageResource {

    @Inject
    S3Client s3;

    protected PutObjectRequest buildPutRequest(String bucketName, String key, String mimeType) {
        return PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(mimeType)
                .build();
    }

    protected GetObjectRequest buildGetRequest(String bucketName, String objectKey) {
        return GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();
    }

    @PUT
    public void saveImage(@PathParam("id") Long productId,
                          @RestForm("file") FileUpload file){

        Product product = Product.findById(productId);

        PutObjectResponse putResponse = s3.putObject(buildPutRequest("ecomarkets", "/products/images",  "image/jpeg" ),
                RequestBody.fromFile(file.uploadedFile()));

    }
}

package ecomarkets.rs.product;

import ecomarkets.domain.core.product.Product;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Path("/product/image")
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


    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public void saveImage(@RestForm("fileTest") FileUpload file){

        Product product = Product.findById(1);

        PutObjectResponse putResponse = s3.putObject(buildPutRequest("ecomarkets", "/products/images",  "image/jpeg" ),
                RequestBody.fromFile(file.uploadedFile()));

    }
}

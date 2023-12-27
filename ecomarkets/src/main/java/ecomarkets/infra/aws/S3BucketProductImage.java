package ecomarkets.infra.aws;

import ecomarkets.domain.core.product.ImageRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.nio.file.Path;

@ApplicationScoped
public class S3BucketProductImage implements ImageRepository {

    @ConfigProperty(name = "bucket.name")
    private String bucketName;

    @Inject
    private S3Client s3;

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
    public void save(Path file){
        PutObjectResponse putResponse = s3.putObject(buildPutRequest("ecomarkets", "/products/images",  "image/jpeg" ),
                RequestBody.fromFile(file));
    }

}

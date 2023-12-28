package ecomarkets.infra.aws;

import ecomarkets.domain.core.product.image.ImageRepository;
import ecomarkets.domain.core.product.image.ProductImage;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.nio.file.Path;
import java.util.List;

@ApplicationScoped
public class S3BucketProductImage implements ImageRepository {

    @ConfigProperty(name = "bucket.name")
    private String bucketName;

    @Inject
    private S3Client s3;

   protected GetObjectRequest buildGetRequest(String bucketName, String objectKey) {
        return GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();
    }
    public void save(Path file,
                     ProductImage productImage) {
        List<Tag> tagsS3 = getTags(productImage);
        s3.putObject(
        PutObjectRequest.builder()
                .bucket(productImage.bucket())
                .key(productImage.key())
                .tagging(Tagging.builder().tagSet(tagsS3).build())
                .build(),
                RequestBody.fromFile(file));
    }

    private List<Tag> getTags(ProductImage productImage) {
//        List<Tag> tagsS3 = productImage.tags().entrySet().stream().map(e -> Tag.builder().key(e.getKey()).value(e.getValue()).build())
//               .collect(Collectors.toList());
//        return tagsS3;
        return null;
    }

    public String getBucketName(){
        return this.bucketName;
    }

}

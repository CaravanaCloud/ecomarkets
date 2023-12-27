package ecomarkets.domain.core.product;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.S3Object;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class ProductImageTest {
    @Inject
    S3Client s3Client;
    @ConfigProperty(name = "bucket.name")
    String bucketName;

    static Product product;

    @BeforeAll
    @Transactional
    static void startLocalStack(){
        product = new ProductBuilder().
                name("Tomate").
                description("Bolo de Banana Fitness (Zero Glúten e Lactose)").
                recipeIngredients("Banana, aveia, Chocolate em pó 50% canela em pó Ovos, granola Açúcar mascavo, Fermento em pó").
                measureUnit(MeasureUnit.UNIT).
                price(10, 50).create();

        product.persist();
    }
    @Test
    public void testS3Resource() {
        CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
                .bucket(bucketName)
                .build();

        s3Client.createBucket(bucketRequest);

        File fileToUpload = new File("src/test/resources/ecomarkets/domain/core/product/acerola.jpg");

        given()
            .multiPart("file", fileToUpload)
            .when()
            .put("/api/product/%d/image".formatted(product.id))
            .then()
            .statusCode(HttpStatus.SC_OK); // Adjust the expected status code as needed

        ListObjectsRequest listRequest = ListObjectsRequest.builder().bucket("ecomarkets").build();

        List<S3Object> r = s3Client.listObjects(listRequest).contents().stream()
                .collect(Collectors.toList());
        r.size();

    }

}

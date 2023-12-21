package ecomarkets.domain.core.product;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Object;

import javax.tools.FileObject;
import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

@QuarkusTest
public class ProductImageTest {
    static DockerImageName localstackImage = DockerImageName.parse("localstack/localstack");

    @Rule
    static LocalStackContainer localstack = new LocalStackContainer(localstackImage)
            .withExposedPorts(4566)
            .withServices(S3);
    static S3Client s3Client;

    static Product product;

    @BeforeAll
    @Transactional
    static void startLocalStack(){
        localstack.start();

        System.out.println("getEndpoint--------------:" + localstack.getEndpoint());
        System.out.println("getAccessKey--------------:" + localstack.getAccessKey());
        System.out.println("getRegion--------------:" + localstack.getRegion());
        System.out.println("getSecretKey--------------:" + localstack.getSecretKey());
        s3Client = S3Client
                .builder()
                .endpointOverride(localstack.getEndpoint())
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(localstack.getAccessKey(), localstack.getSecretKey())
                        )
                )
                .region(Region.of(localstack.getRegion()))
                .build();

        CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
                .bucket("ecomarkets")
                .build();

        s3Client.createBucket(bucketRequest);

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
        File fileToUpload = new File("src/test/resources/ecomarkets/domain/core/product/acerola.jpg");

        given()
            .multiPart("file", fileToUpload)
            .when()
            .put("/api/product/%d/image".formatted(product.id))
            .then()
            .statusCode(200); // Adjust the expected status code as needed

        ListObjectsRequest listRequest = ListObjectsRequest.builder().bucket("ecomarkets").build();

        //HEAD S3 objects to get metadata
        List<S3Object> r = s3Client.listObjects(listRequest).contents().stream()
                .collect(Collectors.toList());
        r.size();

        }

}

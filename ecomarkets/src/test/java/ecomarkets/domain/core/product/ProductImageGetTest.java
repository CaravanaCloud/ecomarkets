package ecomarkets.domain.core.product;

import ecomarkets.domain.core.product.image.ImageRepository;
import ecomarkets.domain.core.product.image.ProductImage;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
public class ProductImageGetTest {
    @ConfigProperty(name = "bucket.name")
    String bucketName;

    static Product product;

    @Inject
    ImageRepository imageRepository;

    @BeforeAll
    @Transactional
    static void startProductFixture(){
        product = new ProductBuilder().
                name("Tomate").
                description("Bolo de Banana Fitness (Zero Glúten e Lactose)").
                recipeIngredients("Banana, aveia, Chocolate em pó 50% canela em pó Ovos, granola Açúcar mascavo, Fermento em pó").
                measureUnit(MeasureUnit.UNIT).
                price(10, 50).create();

        product.persist();
    }
    @Test
    public void testGetS3ProductImage() {
        Path fileToUpload = Paths.get("src/test/resources/ecomarkets/domain/core/product/acerola.jpg");
        ProductImage pi = product.newImage(bucketName);

        imageRepository.save(fileToUpload, pi);

        byte [] file = given()
            .when()
            .get("/api/product/%d/image".formatted(product.id))
            .then()
            .statusCode(HttpStatus.SC_OK)
                .extract()
                .body()
                .asByteArray(); // Adjust the expected status code as needed

        assertThat(file, notNullValue());

        imageRepository.delete(pi);

//        String localFilePath = "/tmp/test.jpg";
//
//        try (FileOutputStream fos = new FileOutputStream(localFilePath)) {
//            fos.write(file);
//            System.out.println("File downloaded and saved at: " + localFilePath);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}

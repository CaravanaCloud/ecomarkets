package ecomarkets.rs.product.image;

import ecomarkets.core.domain.core.product.MeasureUnit;
import ecomarkets.core.domain.core.product.ProductBuilder;
import ecomarkets.core.domain.core.product.image.ProductImage;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.FileOutputStream;
import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
public class ProductImageGetTest extends ProductImageTest{
    @Inject
    S3Client s3Client;

    @BeforeEach
    @Transactional
    void startProductFixture(){
        product = new ProductBuilder().
                name("Tomate").
                description("Bolo de Banana Fitness (Zero Glúten e Lactose)").
                recipeIngredients("Banana, aveia, Chocolate em pó 50% canela em pó Ovos, granola Açúcar mascavo, Fermento em pó").
                measureUnit(MeasureUnit.UNIT).
                price(10, 50).create();

        product.persist();

        ProductImage pi = product.newImage(bucketName,
                fileName,
                mimetype);
    }
    @Test
    public void testGetS3ProductImage() {
        imageRepository.save(file, product.productImage());

        byte [] file = given()
            .when()
            .get("/api/product/%d/image".formatted(product.id))
            .then()
            .statusCode(HttpStatus.SC_OK)
                .extract()
                .body()
                .asByteArray(); // Adjust the expected status code as needed

        assertThat(file, notNullValue());

        imageRepository.delete(product.productImage());

        //saveFileLocalToVerifyManually(file);
    }

    @Test
    public void testPresignedGetUrlS3() {
        ProductImage pi = product.newImage(bucketName,
                fileName,
                mimetype);

        imageRepository.save(file, pi);

        String preAssignedUrl = given()
                .when()
                .get("/api/product/%d/image/presignedGetUrl".formatted(product.id))
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .body()
                .asString();

        byte [] file = given()
                .baseUri(preAssignedUrl)
                .get()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .body()
                .asByteArray();

        assertThat(file, notNullValue());

     //   saveFileLocalToVerifyManually(file);
    }

    private void saveFileLocalToVerifyManually(byte [] file){
        String localFilePath = "/tmp/test.jpg";

        try (FileOutputStream fos = new FileOutputStream(localFilePath)) {
            fos.write(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

package ecomarkets.domain.core.product;

import ecomarkets.domain.core.product.image.ImageRepository;
import ecomarkets.domain.core.product.image.ProductImage;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
public class ProductImagePutTest {
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

    @AfterAll
    static void removeBucket(){
    }
    @Test
    public void testUpdateS3ProductImage() {
        File fileToUpload = new File("src/test/resources/ecomarkets/domain/core/product/acerola.jpg");

        given()
            .multiPart("file", fileToUpload)
            .when()
            .put("/api/product/%d/image".formatted(product.id))
            .then()
            .statusCode(HttpStatus.SC_OK); // Adjust the expected status code as needed

        Product prd = Product.findById(product.id);
        ProductImage img = prd.productImage();
        assertThat(img, notNullValue());
        assertThat(img.bucket(), equalTo(bucketName));
        assertThat(img.key(), equalTo(prd.id.toString()));

        byte [] data = imageRepository.find(img);

        assertThat(data, notNullValue());

        imageRepository.delete(img);
    }
}
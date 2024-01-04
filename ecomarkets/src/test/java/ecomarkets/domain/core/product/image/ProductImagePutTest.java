package ecomarkets.domain.core.product.image;

import ecomarkets.domain.core.product.MeasureUnit;
import ecomarkets.domain.core.product.Product;
import ecomarkets.domain.core.product.ProductBuilder;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
public class ProductImagePutTest extends ProductImageTest {

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
    }

    @Test
    public void testUpdateS3ProductImage() {
        given()
            .multiPart("file", file)
            .multiPart("fileName", fileName)
            .multiPart("mimeType", mimetype)
            .when()
            .put("/api/product/%d/image".formatted(product.id))
            .then()
            .statusCode(HttpStatus.SC_OK); // Adjust the expected status code as needed

        Product prd = Product.findById(product.id);
        ProductImage img = prd.productImage();
        assertThat(img, notNullValue());
        assertThat(img.bucket(), equalTo(bucketName));
        assertThat(img.key(), notNullValue());
        assertThat(img.fileName(), equalTo(fileName));
        assertThat(img.mimeType(), equalTo(mimetype));

        byte [] imageFileData = imageRepository.find(img);
        assertThat(imageFileData, notNullValue());

        imageRepository.delete(img);
    }
}
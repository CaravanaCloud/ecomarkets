package ecomarkets.domain.core.product;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class TestTemp {


    @Test
    public void here(){
        File fileToUpload = new File("src/test/resources/ecomarkets/domain/core/product/acerola.jpg");

        given()
                .multiPart("fileTest", fileToUpload, "image/jpeg")
                .when()
                .post("/product/image")
                .then()
                .statusCode(200); // Adjust the expected status code as needed

    }
}

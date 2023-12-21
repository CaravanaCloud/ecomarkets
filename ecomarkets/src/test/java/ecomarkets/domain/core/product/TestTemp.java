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

        int a = given()
                .multiPart("file", fileToUpload)
                .when()
                .post("/api/product/10/image")
                .statusCode();
                //.statusCode(200); // Adjust the expected status code as needed

        System.out.println(a);
    }
}

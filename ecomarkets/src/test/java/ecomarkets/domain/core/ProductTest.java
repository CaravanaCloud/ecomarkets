package ecomarkets.domain.core;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;


@QuarkusTest
public class ProductTest {

    @BeforeAll
    @Transactional
    static void before(){
        Tenant.persist(Tenant.of("Tenant1", "1"));
    }
    
    @Test
    void createProduct() {
        final String productName = "Abacate";

        given().contentType("application/json")
        .body("{\"name\": \"Abacate\"}")
        .when()
        .post("/api/1/product")
        .then()
        .assertThat()
        .statusCode(HttpStatus.SC_CREATED)
        .body("name", is(productName))
        .body("id", is(notNullValue()));
    }
}

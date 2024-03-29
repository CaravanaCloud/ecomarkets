package ecomarkets.domain.core.product;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;


@QuarkusTest
public class CategoryTest {


    @Test
    void createUpdateCategory() {
        Integer id = given().contentType("application/json")
        .body("""
            {"name": "Bebidas"}
                """)
        .when()
        .post("/api/category")
        .then()
        .assertThat()
        .statusCode(HttpStatus.SC_CREATED)
        .body("id", is(notNullValue()))
        .body("name", is("Bebidas"))
        .extract().body().path("id");

         given().contentType("application/json")
        .body("Chás e Temperos")
        .when()
        .put("/api/category/" + id)
        .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .body("id", is(id))
        .body("name", is("Chás e Temperos"));
    }
    
}

package ecomarkets.domain.core.product;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ecomarkets.domain.core.Tenant;
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
        given().contentType("application/json")
        .body("""
            {
            "name":"Tomate",
            "description":"Bolo de Banana Fitness (Zero Glúten e Lactose)",
            "recipeIngredients":{"description":"Banana, aveia, Chocolate em pó 50% canela em pó Ovos, granola Açúcar mascavo, Fermento em pó"},
            "measureUnit":"UNIT",
            "price":{"unit":10,"cents":50}
            }
                """)
        .when()
        .post("/api/product")
        .then()
        .assertThat()
        .statusCode(HttpStatus.SC_CREATED)
        .body("name", is("Tomate"))
        .body("id", is(notNullValue()));
    }
    
}

package ecomarkets.domain.core.basket;

import ecomarkets.FixtureFactory;
import ecomarkets.domain.core.Tenant;
import ecomarkets.domain.core.partner.Partner;
import ecomarkets.domain.core.product.Product;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import jakarta.transaction.Transactional;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class CreateBasketTest {

    final static Partner PARTNER_JOHN = FixtureFactory.createPartner();
    
    final static Tenant TENANT = Tenant.of("Tenant1", "1");

    static Product prd;

    @BeforeAll
    @Transactional
    static void beforeAll(){
        Tenant.persist(TENANT);        
        Partner.persist(PARTNER_JOHN);

        prd = FixtureFactory.createProduct();

        prd.persist();
    }
    
    @Test
    public void create() {

        final ValidatableResponse vrCreate = given().contentType("application/json")
        .post("/api/basket/" + PARTNER_JOHN.id)
        .then()
        .assertThat();

        Integer id = vrCreate.extract().path("id");
        assertBasket(vrCreate, HttpStatus.SC_CREATED);

        final ValidatableResponse vrGet = given().contentType("application/json")
        .when()
        .get("/api/basket/" + id)
        .then()
        .assertThat();
        
        assertBasket(vrGet, HttpStatus.SC_OK);
    }

    private void assertBasket(ValidatableResponse vr, int httpStatus) {
        vr.statusCode(httpStatus)
        .body("id", is(notNullValue()))
        .body("partnerId.id", is(PARTNER_JOHN.id.intValue()))
        .body("creationDate", is(notNullValue()))
        .body("items.size()", is(0));
    }

    @Test
    @TestTransaction
    public void createBasketItem(){
        final ValidatableResponse vrCreate = given().contentType("application/json")
        .body("""
            [
                {"productId": {"id": %d},
                 "amount": 5}
            ]
        """.formatted(prd.id))
        .when()
        .post("/api/basket/" + PARTNER_JOHN.id)
        .then()
        .assertThat();

        Integer id = vrCreate.extract().path("id");
        Basket basket = Basket.findById(id);
        
        BasketItem basketItem = basket.getItems().iterator().next();
        assertEquals(prd.productId(), basketItem.productId());
        assertEquals(5, basketItem.amount());

    }


}

package ecomarkets.core.domain.core.basket;

import ecomarkets.FixtureFactory;
import ecomarkets.core.domain.core.Tenant;
import ecomarkets.core.domain.core.basket.Basket;
import ecomarkets.core.domain.core.basket.BasketItem;
import ecomarkets.core.domain.core.fair.Fair;
import ecomarkets.core.domain.core.fair.ProductStock;
import ecomarkets.core.domain.core.partner.Partner;
import ecomarkets.core.domain.core.product.Product;
import ecomarkets.core.domain.notification.email.EmailPendingToSend;
import io.quarkus.test.InjectMock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import jakarta.transaction.Transactional;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class CreateBasketTest {

    Partner partner = FixtureFactory.createPartner();
    
    final static Tenant TENANT = Tenant.of("Tenant1", "1");

    Product prd;

    Fair fair;

    @InjectMock
    ProductStock productStock;

    @BeforeEach
    @Transactional
    void before(){
        partner.persist();

        prd = FixtureFactory.createProduct();
        prd.persist();

        fair = FixtureFactory.createFair();
        fair.persist();

        Mockito.when(productStock.getAmountProductAvailable(fair.fairId(), prd.productId())).thenReturn(10.0);
    }

    @AfterAll
    @Transactional
    public static void deleteEmailPending(){
        EmailPendingToSend.deleteAll();
    }
    
    @Test
    public void create() {

        final ValidatableResponse vrCreate = given().contentType("application/json")
                .body("""
               {"fairId": {"id": %d},
                "partnerId": {"id": %d}}
        """.formatted(fair.id, partner.id))
                .post("/api/basket/")
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
        .body("partnerId.id", is(partner.id.intValue()))
        .body("fairId.id", is(fair.id.intValue()))
        .body("creationDate", is(notNullValue()))
        .body("items.size()", is(0));
    }

    @Test
    @TestTransaction
    public void createBasketItem(){
        final ValidatableResponse vrCreate = given().contentType("application/json")
        .body("""
             {"fairId": {"id": %d},
              "partnerId": {"id": %d},
              "items":
               [
                {"productId": {"id": %d},
                 "amount": 5}
               ] 
             }
        """.formatted(fair.id, partner.id, prd.id))
        .when()
        .post("/api/basket/")
        .then()
        .assertThat();

        Integer id = vrCreate.extract().path("id");
        Basket basket = Basket.findById(id);
        
        BasketItem basketItem = basket.getItems().iterator().next();
        assertEquals(prd.productId(), basketItem.productId());
        assertEquals(5, basketItem.amount());

    }


}

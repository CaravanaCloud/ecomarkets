package ecomarkets.domain.core.basket;

import ecomarkets.FixtureFactory;
import ecomarkets.domain.core.Tenant;
import ecomarkets.domain.core.basket.event.BasketReservedEvent;
import ecomarkets.domain.core.fair.Fair;
import ecomarkets.domain.core.fair.ProductStock;
import ecomarkets.domain.core.partner.Partner;
import ecomarkets.domain.core.product.Product;
import ecomarkets.domain.notification.email.EmailPendingToSend;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import jakarta.transaction.Transactional;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@QuarkusTest
public class ReserveBasketTest {
    final Partner partner = FixtureFactory.createPartner();

    Product product;

    Basket basket;

    @InjectMock
    ProductStock productStock;

    final Tenant TENANT = Tenant.of("Tenant1", "1");


    @BeforeEach
    @Transactional
    void beforeAll(){
        Tenant.persist(TENANT);
        partner.persist();

        product = FixtureFactory.createProduct();
        product.persist();

        Fair fair = FixtureFactory.createFair();
        fair.persist();

        basket = Basket.of(fair.fairId(), partner.partnerId());

        Mockito.when(productStock.getAmountProductAvailable(basket.getFairId(), product.productId())).thenReturn(10.0);

        basket.addItem(productStock, product, 5);

        basket.persist();
    }

    @AfterAll
    @Transactional
    public static void deleteEmailPending(){
        EmailPendingToSend.deleteAll();
    }

    @Test
    public void testReserveBasket(){
        final ValidatableResponse vrCreate = given().contentType("application/json")
                .put("/api/basket/" + basket.id + "/reserve")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat();

        Basket basketFromDB = Basket.findById(basket.id);
        BasketReservedEvent event = BasketReservedEvent.find("basketId", basket.basketId()).firstResult();
        assertThat(event, notNullValue());
    }

}

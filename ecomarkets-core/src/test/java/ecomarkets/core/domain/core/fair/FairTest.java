package ecomarkets.core.domain.core.fair;

import ecomarkets.core.FixtureFactory;
import ecomarkets.core.domain.core.fair.Fair;
import ecomarkets.core.domain.core.fair.FarmerProductAvailableInFair;
import ecomarkets.core.domain.core.fair.ShoppingPeriod;
import ecomarkets.core.domain.core.farmer.Farmer;
import ecomarkets.core.domain.core.product.Product;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
public class FairTest {

    Product product;


    Farmer farmer;


    Fair fair = FixtureFactory.createFair();


    @BeforeEach
    @Transactional
    public void before(){
        product = FixtureFactory.createProduct();
        product.persist();

        farmer = FixtureFactory.createFarmer();
        farmer.persist();

        fair = FixtureFactory.createFair();
        fair.persist();
    }

    @Test
    @TestTransaction
    public void addProductTest(){
        final Integer AMOUNT = 10;

        Integer id = given().contentType("application/json")
                .body("""
                        {
                            "farmerId": {"id": %d},
                            "amount": %d
                        }
                        """.formatted(farmer.farmerId().id(), AMOUNT))
                .when()
                .post("/api/fair/%d/product/%d".formatted(fair.id, product.id))
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("id", is(notNullValue()))
                .extract().path("id");

        FarmerProductAvailableInFair farmerProductAvailableInFair = FarmerProductAvailableInFair.findById(id);
        assertThat(farmerProductAvailableInFair.getFairId(), Matchers.is(fair.fairId()));
        assertThat(farmerProductAvailableInFair.getProductId(), Matchers.is(product.productId()));
        assertThat(farmerProductAvailableInFair.getFarmerId(), Matchers.is(farmer.farmerId()));
        assertThat(farmerProductAvailableInFair.getAmount(), Matchers.is(AMOUNT));
    }

    @Test
    public void testValidityPeriod(){
        assertThrows(RuntimeException.class, () -> {
            ShoppingPeriod.of(LocalDateTime.now(), LocalDateTime.now().plusWeeks(-1));
        });
    }
}

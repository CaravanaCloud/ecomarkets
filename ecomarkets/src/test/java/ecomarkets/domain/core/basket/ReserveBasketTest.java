package ecomarkets.domain.core.basket;

import ecomarkets.domain.core.Tenant;
import ecomarkets.domain.core.partner.Partner;
import ecomarkets.domain.core.product.MeasureUnit;
import ecomarkets.domain.core.product.Product;
import ecomarkets.domain.core.product.ProductBuilder;
import ecomarkets.domain.register.Address;
import ecomarkets.domain.register.CPF;
import ecomarkets.domain.register.EmailAddress;
import ecomarkets.domain.register.Telephone;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import jakarta.transaction.Transactional;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@QuarkusTest
public class ReserveBasketTest {

    final static Partner PARTNER_JOHN = Partner.of("Joao",
            CPF.of("12122112") ,
            EmailAddress.of("joao@gmail.com"),
            LocalDate.now(),
            Telephone.of("27", "123456789"),
            Address.of("Brasil",
                    "Espirito Santo",
                    "Vitória",
                    123,
                    "Apt 123",
                    "Perto da...",
                    123456));

    static Product product;
    static Basket basket;

    final static Tenant TENANT = Tenant.of("Tenant1", "1");

    @BeforeAll
    @Transactional
    static void beforeAll(){
        Tenant.persist(TENANT);
        Partner.persist(PARTNER_JOHN);

        product = new ProductBuilder().
                name("Bolo de Banana").
                description("Bolo de Banana Fitness (Zero Glúten e Lactose)").
                recipeIngredients("Banana, aveia, Chocolate em pó 50% canela em pó Ovos, granola Açúcar mascavo, Fermento em pó").
                measureUnit(MeasureUnit.UNIT).
                price(10, 50).create();

        product.persist();

        basket = Basket.of(PARTNER_JOHN.partnerId());

        basket.addItem(product, 5);

        basket.persist();
    }

    @Test
    public void testReserveBasket(){

        final ValidatableResponse vrCreate = given().contentType("application/json")
                .put("/api/basket/" + basket.id + "/reserve")
                .then()
                .body("reservedDate", is(notNullValue()))
                .statusCode(HttpStatus.SC_OK)
                .assertThat();

        Basket basketFromDB = Basket.findById(basket.id);
        assertThat(basketFromDB.getReservedDate(), notNullValue());
    }




}

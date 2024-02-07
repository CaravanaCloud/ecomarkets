package ecomarkets.domain.core.basket;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import ecomarkets.domain.core.Tenant;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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

@QuarkusTest
public class CreateBasketTest {

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
    
    final static Tenant TENANT = Tenant.of("Tenant1", "1");

    static Product prd;

    @BeforeAll
    @Transactional
    static void beforeAll(){
        Tenant.persist(TENANT);        
        Partner.persist(PARTNER_JOHN);

        prd = new ProductBuilder().
                name("Tomate").
                description("Bolo de Banana Fitness (Zero Glúten e Lactose)").
                recipeIngredients("Banana, aveia, Chocolate em pó 50% canela em pó Ovos, granola Açúcar mascavo, Fermento em pó").
                measureUnit(MeasureUnit.UNIT).
                price(10, 50).create();

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

package ecomarkets.domain.core;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;


@QuarkusTest
public class PartnerTest {

    final String PARTNER = 
     "{\"name\":\"Joao\","+
     "\"cpf\":{\"value\":12122112},"+
     "\"email\":{\"value\":\"joao@gmail.com\"},"+
     "\"birthDate\":\"2023-12-09\","+
     "\"address\":"+
        "{\"id\":null,"+
        "\"country\":null,"+
        "\"state\":\"Espirito Santo\","+
        "\"city\":\"Vitória\","+
        "\"houseNumber\":123,"+
        "\"addOn\":\"Apt 123\","+
        "\"reference\":\"Perto da...\","+
        "\"postCode\":123456}}";

    @Test
    public void create() throws Exception {

         given().contentType("application/json")
        .body(PARTNER)
        .when()
        .post("/api/partner")
        .then()
        .assertThat()
        .statusCode(HttpStatus.SC_CREATED)
        .body("id", is(notNullValue()))
        .body("name", is("Joao"));

    }
}

package ecomarkets.domain.core;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;


@QuarkusTest
public class PartnerTest {

    final String PARTNER = 
     "{\"name\":\"Joao\","+
     "\"cpf\":{\"value\":123456789},"+
     "\"email\":{\"value\":\"joao@gmail.com\"},"+
     "\"birthDate\":\"2023-12-09\","+
     "\"address\":"+
        "{\"id\":null,"+
        "\"country\":\"Brasil\","+
        "\"state\":\"Espirito Santo\","+
        "\"city\":\"Vitória\","+
        "\"houseNumber\":123,"+
        "\"addOn\":\"Apt 100\","+
        "\"reference\":\"Perto da mercearia do tio zé\","+
        "\"postCode\":123456}}";

    @Test
    public void create() throws Exception {

        final ValidatableResponse vrCreate = given().contentType("application/json")
        .body(PARTNER)
        .when()
        .post("/api/partner")
        .then()
        .assertThat();
        assertPartner(vrCreate, HttpStatus.SC_CREATED);

        Integer id = vrCreate.extract().path("id");
        
        ValidatableResponse vrGet = given().contentType("application/json")
        .body(PARTNER)
        .when()
        .get("/api/partner/" + id)
        .then()
        .assertThat();
        assertPartner(vrGet, HttpStatus.SC_OK);
    }

    private void assertPartner(ValidatableResponse vr, int httpStatus) {
        vr.statusCode(httpStatus)
        .body("id", is(notNullValue()))
        .body("name", is("Joao"))
        .body("cpf.value", is(123456789))
        .body("email.value", is("joao@gmail.com"))
        .body("birthDate", is("2023-12-09"))
        .body("address.id", is(notNullValue()))
        .body("address.country", is("Brasil"))
        .body("address.state", is("Espirito Santo"))
        .body("address.houseNumber", is(123))
        .body("address.addOn", is("Apt 100"))
        .body("address.reference", is("Perto da mercearia do tio zé"))
        .body("address.postCode", is(123456));
    }
}

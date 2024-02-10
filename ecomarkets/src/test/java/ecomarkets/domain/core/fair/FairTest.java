package ecomarkets.domain.core.fair;

import ecomarkets.FixtureFactory;
import ecomarkets.domain.core.basket.Basket;
import ecomarkets.domain.core.farmer.Farmer;
import ecomarkets.domain.core.partner.Partner;
import ecomarkets.domain.core.product.Product;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
public class FairTest {



    @Test
    public void testValidityPeriod(){
        assertThrows(RuntimeException.class, () -> {
            ShoppingPeriod.of(LocalDateTime.now(), LocalDateTime.now().plusWeeks(-1));
        });
    }
}

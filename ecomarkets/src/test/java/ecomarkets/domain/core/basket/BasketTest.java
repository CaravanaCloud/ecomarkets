package ecomarkets.domain.core.basket;

import ecomarkets.FixtureFactory;
import ecomarkets.domain.core.partner.PartnerId;
import ecomarkets.domain.core.product.Product;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class BasketTest {

    @Test
    @TestTransaction
    public void testPaymentFormatted(){
        Basket basket = Basket.of(FixtureFactory.createFair().fairId(), PartnerId.of(1l));

        Product product = FixtureFactory.createProductBuilder().price(5, 15).create();
        basket.addItem(product, 10);

        assertEquals(51.5, basket.totalPayment());

    }
}

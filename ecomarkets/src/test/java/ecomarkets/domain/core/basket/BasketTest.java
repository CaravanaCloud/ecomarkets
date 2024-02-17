package ecomarkets.domain.core.basket;

import ecomarkets.FixtureFactory;
import ecomarkets.domain.core.partner.PartnerId;
import ecomarkets.domain.core.product.Product;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

@QuarkusTest
public class BasketTest {

    @Test
    @TestTransaction
    public void testPaymentFormatted(){
        Basket basket = Basket.of(FixtureFactory.createFair().fairId(), PartnerId.of(1l));

        Product product = FixtureFactory.createProductBuilder().price(5, 15).create();
        basket.addItem(product, 10);

        final BigDecimal error = new BigDecimal(0.001);
        assertThat(new BigDecimal(51.5), closeTo(basket.totalPayment(), error));
    }
}

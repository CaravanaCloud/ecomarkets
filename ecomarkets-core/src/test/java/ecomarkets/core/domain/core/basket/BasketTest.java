package ecomarkets.core.domain.core.basket;

import ecomarkets.FixtureFactory;
import ecomarkets.core.domain.core.basket.Basket;
import ecomarkets.core.domain.core.fair.ProductStock;
import ecomarkets.core.domain.core.partner.PartnerId;
import ecomarkets.core.domain.core.product.Product;
import io.quarkus.test.InjectMock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

@QuarkusTest
public class BasketTest {

    @InjectMock
    ProductStock productStock;

    @Test
    @TestTransaction
    public void testPaymentFormatted(){
        Basket basket = Basket.of(FixtureFactory.createFair().fairId(), PartnerId.of(1l));
        basket.persist();

        Product product = FixtureFactory.createProductBuilder().price(5, 15).create();
        product.persist();

        Mockito.when(productStock.getAmountProductAvailable(basket.getFairId(), product.productId())).thenReturn(10.0);

        basket.addItem(productStock, product, 10);

        final BigDecimal error = new BigDecimal(0.001);
        assertThat(new BigDecimal(51.5), closeTo(basket.totalPayment(), error));
    }
}

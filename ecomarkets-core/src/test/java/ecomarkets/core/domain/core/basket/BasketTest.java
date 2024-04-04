package ecomarkets.core.domain.core.basket;

import ecomarkets.core.FixtureFactory;
import ecomarkets.core.domain.core.fair.ProductStock;
import ecomarkets.core.domain.core.partner.PartnerId;
import ecomarkets.core.domain.core.product.Product;
import io.quarkus.test.InjectMock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        assertEquals(new BigDecimal("51.50"), basket.totalPayment());
    }
}

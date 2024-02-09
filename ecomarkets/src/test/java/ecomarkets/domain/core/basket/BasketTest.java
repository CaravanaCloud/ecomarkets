package ecomarkets.domain.core.basket;

import ecomarkets.FixtureFactory;
import ecomarkets.domain.core.partner.PartnerId;
import ecomarkets.domain.core.product.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BasketTest {

    @Test
    public void testPaymentFormatted(){
        Basket basket = Basket.of(PartnerId.of(1l));

        Product product = FixtureFactory.getProductBuilder().price(5, 15).create();
        basket.addItem(product, 10);

        assertEquals(51.5, basket.totalPayment());

    }
}

package ecomarkets.domain.core.fair;

import ecomarkets.FixtureFactory;
import ecomarkets.domain.core.basket.Basket;
import ecomarkets.domain.core.farmer.Farmer;
import ecomarkets.domain.core.partner.Partner;
import ecomarkets.domain.core.product.Product;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class ProductAvailableInFairTest {

    @Test
    @TestTransaction
    void createProductStock() {
        Product prd = FixtureFactory.createProduct();
        prd.persist();

        Farmer farmer = FixtureFactory.createFarmer();
        farmer.persist();

        Fair fair = FixtureFactory.createFair();
        fair.persist();

        ProductAvailableInFair stockBefore = FixtureFactory.createProductAvailableInFair(fair.fairId(), farmer.farmerId(), prd.productId(), 100);
        stockBefore.persist();

        ProductAvailableInFair stock = ProductAvailableInFair.findById(stockBefore.id);

        assertEquals(stockBefore.id, stock.id);
        assertEquals(stockBefore.getFarmerId(), stock.getFarmerId());
        assertEquals(stockBefore.getProductId(), stock.getProductId());
        assertEquals(stockBefore.getAmount(), stock.getAmount());

    }

    @Test
    @TestTransaction
    public void testAvailableStock(){

        Product prd = FixtureFactory.createProduct();
        prd.persist();

        Farmer farmer = FixtureFactory.createFarmer();
        farmer.persist();

        Fair fair = FixtureFactory.createFair();
        fair.persist();

        ProductAvailableInFair stock = FixtureFactory.createProductAvailableInFair(fair.fairId(), farmer.farmerId(), prd.productId(), 10);
        stock.persist();

        Partner partner = FixtureFactory.createPartner();
        partner.persist();

        Basket basket = Basket.of(fair.fairId(), partner.partnerId());
        basket.addItem(prd, 8);
        basket.persist();

        Double result = ProductAvailableInFair.getAmountProductAvailable(fair.fairId(), prd.productId());

        assertEquals(2, result);

    }

}

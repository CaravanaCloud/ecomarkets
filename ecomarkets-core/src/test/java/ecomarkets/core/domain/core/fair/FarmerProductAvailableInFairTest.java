package ecomarkets.core.domain.core.fair;

import ecomarkets.FixtureFactory;
import ecomarkets.core.domain.core.basket.Basket;
import ecomarkets.core.domain.core.fair.Fair;
import ecomarkets.core.domain.core.fair.FarmerProductAvailableInFair;
import ecomarkets.core.domain.core.fair.ProductStock;
import ecomarkets.core.domain.core.farmer.Farmer;
import ecomarkets.core.domain.core.partner.Partner;
import ecomarkets.core.domain.core.product.Product;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class FarmerProductAvailableInFairTest {

    @Inject
    ProductStock productStock;

    @Test
    @TestTransaction
    void createProductStock() {
        Product prd = FixtureFactory.createProduct();
        prd.persist();

        Farmer farmer = FixtureFactory.createFarmer();
        farmer.persist();

        Fair fair = FixtureFactory.createFair();
        fair.persist();

        FarmerProductAvailableInFair stockBefore = FixtureFactory.createProductAvailableInFair(fair.fairId(), farmer.farmerId(), prd.productId(), 100);
        stockBefore.persist();

        FarmerProductAvailableInFair stock = FarmerProductAvailableInFair.findById(stockBefore.id);

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

        FarmerProductAvailableInFair stock = FixtureFactory.createProductAvailableInFair(fair.fairId(), farmer.farmerId(), prd.productId(), 10);
        stock.persist();

        Partner partner = FixtureFactory.createPartner();
        partner.persist();

        Basket basket = Basket.of(fair.fairId(), partner.partnerId());
        basket.addItem(productStock, prd, 8);
        basket.persist();

        Double result = productStock.getAmountProductAvailable(fair.fairId(), prd.productId());

        assertEquals(2, result);

    }

}

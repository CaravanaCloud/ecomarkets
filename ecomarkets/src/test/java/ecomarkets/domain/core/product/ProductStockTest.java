package ecomarkets.domain.core.product;

import ecomarkets.FixtureFactory;
import ecomarkets.domain.core.basket.Basket;
import ecomarkets.domain.core.farmer.Farmer;
import ecomarkets.domain.core.partner.Partner;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class ProductStockTest {

    @Test
    @TestTransaction
    void createProductStock() {
        Product prd = FixtureFactory.createProduct();

        prd.persist();

        Farmer farmer = FixtureFactory.createFarmer();

        farmer.persist();

        ProductStock stockBefore = ProductStock.of(farmer.farmerId(), prd, 100);
        stockBefore.persist();

        ProductStock stock = ProductStock.findById(stockBefore.id);

        assertEquals(stockBefore.id, stock.id);
        assertEquals(stockBefore.getFarmerId(), stock.getFarmerId());
        assertEquals(stockBefore.getProduct().getName(), stock.getProduct().getName());
        assertEquals(stockBefore.getAmount(), stock.getAmount());

    }

    @Test
    @TestTransaction
    public void testAvailableStock(){

        Product prd = FixtureFactory.createProduct();
        prd.persist();

        Farmer farmer = FixtureFactory.createFarmer();
        farmer.persist();

        ProductStock stock = ProductStock.of(farmer.farmerId(), prd, 10);
        stock.persist();

        Partner partner = FixtureFactory.createPartner();
        partner.persist();

        Basket basket = Basket.of(partner.partnerId());
        basket.addItem(prd, 8);
        basket.persist();

        Double result = ProductStock.getAvailableStock(prd.productId());

        assertEquals(2, result);

    }
}

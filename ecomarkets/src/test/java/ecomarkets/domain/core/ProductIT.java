package ecomarkets.domain.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ecomarkets.domain.core.farmer.Farmer;
import ecomarkets.domain.core.product.Product;
import ecomarkets.domain.core.product.ProductBuilder;
import ecomarkets.domain.core.product.ProductStock;
import ecomarkets.domain.core.product.MeasureUnit;
import ecomarkets.domain.register.Address;
import ecomarkets.domain.register.Email;
import ecomarkets.domain.register.Telephone;
import io.quarkus.test.junit.QuarkusIntegrationTest;

@QuarkusIntegrationTest
public class ProductIT {

    @Test
    void createProductStock() {
        Product prd = new ProductBuilder().
        name("Tomate").
        description("Bolo de Banana Fitness (Zero Glúten e Lactose)").
        recipeIngredients("Banana, aveia, Chocolate em pó 50% canela em pó Ovos, granola Açúcar mascavo, Fermento em pó").
        measureUnit(MeasureUnit.UNIT).
        price(10, 50).create();

        prd.persist();

        Tenant tenant = Tenant.of("teste", "1");
        tenant.persist();

        Farmer farmer = Farmer.of("Maria",
        Email.of("maria@gmail.com"), 
        Telephone.of(27, 123456789), 
        Address.of("Brasil", 
        "Espirito Santo", 
        "Vitória",
        123,
        "Apt 123",
        "Perto da...",
        123456) );

        farmer.persist();

        ProductStock stockBefore = ProductStock.of(tenant, farmer.farmerId(), prd, 100);
        stockBefore.persist();

        ProductStock stock = ProductStock.findById(stockBefore.id);

        assertEquals(stockBefore.id, stock.id);
        assertEquals(stockBefore.getFarmerId(), stock.getFarmerId());
        assertEquals(stockBefore.getProduct().getName(), stock.getProduct().getName());
        assertEquals(stockBefore.getAmount(), stock.getAmount());

    }
}

package ecomarkets;

import ecomarkets.domain.core.fair.Fair;
import ecomarkets.domain.core.fair.FairId;
import ecomarkets.domain.core.ProductAvailableInFair;
import ecomarkets.domain.core.fair.ShoppingPeriod;
import ecomarkets.domain.core.farmer.Farmer;
import ecomarkets.domain.core.farmer.FarmerId;
import ecomarkets.domain.core.partner.Partner;
import ecomarkets.domain.core.product.MeasureUnit;
import ecomarkets.domain.core.product.Product;
import ecomarkets.domain.core.product.ProductBuilder;
import ecomarkets.domain.core.product.ProductId;
import ecomarkets.domain.register.Address;
import ecomarkets.domain.register.CPF;
import ecomarkets.domain.register.EmailAddress;
import ecomarkets.domain.register.Telephone;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class FixtureFactory {

   public static Product createProduct() {
        return getProductBuilder().create();
    }

    public static ProductBuilder getProductBuilder() {
        return new ProductBuilder().
                name("Bolo de Banana").
                description("Bolo de Banana Fitness (Zero Glúten e Lactose)").
                recipeIngredients("Banana, aveia, Chocolate em pó 50% canela em pó Ovos, granola Açúcar mascavo, Fermento em pó").
                measureUnit(MeasureUnit.UNIT).
                price(10, 50);
    }

    public static ProductAvailableInFair getProductStock(FairId fairId, FarmerId farmerId, ProductId productId, Integer amount){
        return ProductAvailableInFair.of(fairId, farmerId, productId, amount);
    }

    public static Fair getFair(){
        ShoppingPeriod shoppingPeriod = ShoppingPeriod.of(LocalDateTime.now(), LocalDateTime.now().plusWeeks(1));
        return new Fair(shoppingPeriod);
    }

    public static Farmer createFarmer() {
        return Farmer.of("Maria",
                EmailAddress.of("maria@gmail.com"),
                Telephone.of("27", "123456789"),
                Address.of("Brasil",
                        "Espirito Santo",
                        "Vitória",
                        123,
                        "Apt 123",
                        "Perto da...",
                        123456));
    }

    public static Partner createPartner() {
        return Partner.of("Joao",
                CPF.of("12122112"),
                EmailAddress.of("joao@gmail.com"),
                LocalDate.now(),
                Telephone.of("27", "123456789"),
                Address.of("Brasil",
                        "Espirito Santo",
                        "Vitória",
                        123,
                        "Apt 123",
                        "Perto da...",
                        123456));
    }
}

package ecomarkets.core.domain.core.fair.delivery;

import ecomarkets.core.domain.core.fair.FairId;
import ecomarkets.core.domain.core.fair.FarmerProductAvailableInFair;
import ecomarkets.core.domain.core.farmer.FarmerId;
import ecomarkets.core.domain.core.product.ProductId;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DivideProductsToFarmersTest {

    @Test
    public void testOneProductOneBasketEqualDivision(){
        FairId fairId = FairId.of(10l);
        FarmerId farmerId = FarmerId.of(1l);
        ProductId productId = ProductId.of(1l);

        Map<ProductId, Integer> productsAmountInBaskets = new HashMap<>();
        productsAmountInBaskets.put(productId, 10);

        List<FarmerProductAvailableInFair> productsAmountInFair = new ArrayList<>();
        productsAmountInFair.add(FarmerProductAvailableInFair.of(fairId, farmerId, productId, 10));

        List<FarmerProductionToDeliver> result = new DivideProductsToFarmers().divideProductionForEachFarmer(fairId, productsAmountInBaskets, productsAmountInFair);

        assertEquals(1, result.size());

        FarmerProductionToDeliver farmerProductionToDeliver = result.iterator().next();

        assertEquals(10, farmerProductionToDeliver.amountToDeliver());
    }

    @Test
    public void testNFarmersOneBasketEqualDivision(){
        FairId fairId = FairId.of(10l);
        FarmerId farmer1 = FarmerId.of(1l);
        FarmerId farmer2 = FarmerId.of(1l);
        ProductId productId = ProductId.of(1l);

        Map<ProductId, Integer> productsAmountInBaskets = new HashMap<>();
        productsAmountInBaskets.put(productId, 10);

        List<FarmerProductAvailableInFair> productsAmountInFair = new ArrayList<>();
        productsAmountInFair.add(FarmerProductAvailableInFair.of(fairId, farmer1, productId, 10));
        productsAmountInFair.add(FarmerProductAvailableInFair.of(fairId, farmer2, productId, 10));

        List<FarmerProductionToDeliver> result = new DivideProductsToFarmers().divideProductionForEachFarmer(fairId, productsAmountInBaskets, productsAmountInFair);

        assertEquals(2, result.size());

        FarmerProductionToDeliver farmerProductionToDeliver1 = result.iterator().next();
        assertEquals(5, farmerProductionToDeliver1.amountToDeliver());

        FarmerProductionToDeliver farmerProductionToDeliver2 = result.iterator().next();
        assertEquals(5, farmerProductionToDeliver1.amountToDeliver());
    }

    @Test
    public void testNFarmersOneBasketNonEqualDivision(){
        FairId fairId = FairId.of(10l);
        FarmerId farmer1 = FarmerId.of(1l);
        FarmerId farmer2 = FarmerId.of(2l);
        ProductId productId = ProductId.of(1l);

        Map<ProductId, Integer> productsAmountInBaskets = new HashMap<>();
        productsAmountInBaskets.put(productId, 3);

        List<FarmerProductAvailableInFair> productsAmountInFair = new ArrayList<>();
        productsAmountInFair.add(FarmerProductAvailableInFair.of(fairId, farmer1, productId, 8));
        productsAmountInFair.add(FarmerProductAvailableInFair.of(fairId, farmer2, productId, 10));

        List<FarmerProductionToDeliver> result = new DivideProductsToFarmers().divideProductionForEachFarmer(fairId, productsAmountInBaskets, productsAmountInFair);

        assertEquals(2, result.size());

        FarmerProductionToDeliver farmerProductionToDeliver1 = result.stream().filter(p -> p.farmerId().equals(farmer1)).findFirst().get();
        assertEquals(1, farmerProductionToDeliver1.amountToDeliver());

        FarmerProductionToDeliver farmerProductionToDeliver2 = result.stream().filter(p -> p.farmerId().equals(farmer2)).findFirst().get();
        assertEquals(2, farmerProductionToDeliver2.amountToDeliver());
    }

    @Test
    public void testNFarmersNBaskets(){
        FairId fairId = FairId.of(10l);
        FarmerId farmer1 = FarmerId.of(1l);
        FarmerId farmer2 = FarmerId.of(2l);
        ProductId productId1 = ProductId.of(1l);
        ProductId productId2 = ProductId.of(2l);

        Map<ProductId, Integer> productsAmountInBaskets = new HashMap<>();
        productsAmountInBaskets.put(productId1, 3);
        productsAmountInBaskets.put(productId2, 5);

        List<FarmerProductAvailableInFair> productsAmountInFair = new ArrayList<>();
        productsAmountInFair.add(FarmerProductAvailableInFair.of(fairId, farmer1, productId1, 8));
        productsAmountInFair.add(FarmerProductAvailableInFair.of(fairId, farmer2, productId1, 10));
        productsAmountInFair.add(FarmerProductAvailableInFair.of(fairId, farmer1, productId2, 15));
        productsAmountInFair.add(FarmerProductAvailableInFair.of(fairId, farmer2, productId2, 7));

        List<FarmerProductionToDeliver> result = new DivideProductsToFarmers().divideProductionForEachFarmer(fairId, productsAmountInBaskets, productsAmountInFair);

        assertEquals(4, result.size());

        FarmerProductionToDeliver farmer1Production1ToDeliver = result.stream().filter(p -> p.farmerId().equals(farmer1) && p.productId().equals(productId1)).findFirst().get();
        assertEquals(1, farmer1Production1ToDeliver.amountToDeliver());

        FarmerProductionToDeliver farmer2Production1ToDeliver = result.stream().filter(p -> p.farmerId().equals(farmer2) && p.productId().equals(productId1)).findFirst().get();
        assertEquals(2, farmer2Production1ToDeliver.amountToDeliver());

        FarmerProductionToDeliver farmer1Production2ToDeliver = result.stream().filter(p -> p.farmerId().equals(farmer1) && p.productId().equals(productId2)).findFirst().get();
        assertEquals(3, farmer1Production2ToDeliver.amountToDeliver());

        FarmerProductionToDeliver farmer2Production2ToDeliver = result.stream().filter(p -> p.farmerId().equals(farmer2) && p.productId().equals(productId2)).findFirst().get();
        assertEquals(2, farmer2Production2ToDeliver.amountToDeliver());
    }
}

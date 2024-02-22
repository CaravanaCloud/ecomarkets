package ecomarkets.domain.core.fair.report;

import ecomarkets.domain.core.basket.Basket;
import ecomarkets.domain.core.basket.BasketItem;
import ecomarkets.domain.core.fair.FairId;
import ecomarkets.domain.core.fair.FarmerProductAvailableInFair;
import ecomarkets.domain.core.product.ProductId;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class DivideProductsToFarmers {

    /**
     * PRECONDITION: Ensure there is a unique constraint for the combination of fairId, farmerId, and productId fields.
     * This means, for each product produced by a farmer to a fair will have an amount.
     */
    public List<FarmerProductionToDeliver> process(FairId fairId){
        List<FarmerProductAvailableInFair> productsAmountInFair =  FarmerProductAvailableInFair.find("fairId", fairId).list();

        Map<ProductId, Integer> productsAmountInBaskets = findProductAmountInBaskets(fairId);

        return divideProductionForEachFarmer(fairId, productsAmountInBaskets, productsAmountInFair);
    }

    private List<FarmerProductionToDeliver> divideProductionForEachFarmer(FairId fairId, Map<ProductId, Integer> productsAmountInBaskets,
                                               List<FarmerProductAvailableInFair> productsAmountInFair) {
        List<FarmerProductionToDeliver> result = new ArrayList<>();

        for(Map.Entry<ProductId, Integer> entry : productsAmountInBaskets.entrySet()){
            final ProductId productId = entry.getKey();
            final int sumAmountProductInBaskets = entry.getValue();

            List<FarmerProductAvailableInFair> productsInAscendingAmountOrder = filterByProductInAscendingAmount(productId, productsAmountInFair);
            result.addAll(divideProduct(productsInAscendingAmountOrder, sumAmountProductInBaskets));
        }

        return result;
    }

    private List<FarmerProductionToDeliver> divideProduct(List<FarmerProductAvailableInFair> productsInAscendingAmountOrder, int sumAmountProductInBaskets) {
        List<FarmerProductionToDeliver> result = new ArrayList<>();

        int qtFarmers = productsInAscendingAmountOrder.size();
        int qtToDeliverForEachFarmer = Math.ceilDiv(sumAmountProductInBaskets, qtFarmers);
        int totalDelivered = 0;
        for(FarmerProductAvailableInFair item : productsInAscendingAmountOrder){
            if(totalDelivered >= sumAmountProductInBaskets){
                break;
            }

            final int amountAvailableInFairByFarmer = item.getAmount();

            final int amountToDeliver;

            final int rest;
            if(amountAvailableInFairByFarmer >= qtToDeliverForEachFarmer){
                amountToDeliver = qtToDeliverForEachFarmer;
                rest = 0;
            }else{
                amountToDeliver = amountAvailableInFairByFarmer;
                rest = qtToDeliverForEachFarmer - amountAvailableInFairByFarmer;
            }
            qtFarmers--;
            qtToDeliverForEachFarmer += (rest / qtFarmers);

            result.add(FarmerProductionToDeliver.of(item.getFairId(),
                    item.getFarmerId(),
                    item.getProductId(),
                    amountToDeliver));

            totalDelivered += amountToDeliver;
        }

        if(qtToDeliverForEachFarmer > 0){
            // TODO: Implement notification for reporting inconsistencies in production division among farmers caused by stock faults.
        }

        return result;
    }

    private Map<ProductId, Integer> findProductAmountInBaskets(FairId fairId) {
        if(fairId == null){
            throw new IllegalArgumentException("fairId should not be null");
        }

        List<Basket> baskets = Basket.find("fairId", fairId).list();

        Map<ProductId, Integer> result = baskets.stream()
                .flatMap(b -> b.getItems().stream())
                .collect(Collectors.groupingBy(
                        item -> item.productId(),
                        Collectors.summingInt(BasketItem::amount)
                ));

        return result;
    }

    private List<FarmerProductAvailableInFair> filterByProductInAscendingAmount(ProductId productId, List<FarmerProductAvailableInFair> productsAvailableInFair){
        return productsAvailableInFair.stream().filter(p -> p.getProductId().equals(productId)).sorted(Comparator.comparing(FarmerProductAvailableInFair::getAmount)).toList();
    }

}

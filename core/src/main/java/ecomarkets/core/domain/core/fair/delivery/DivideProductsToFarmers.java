package ecomarkets.core.domain.core.fair.delivery;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ecomarkets.core.domain.core.basket.Basket;
import ecomarkets.core.domain.core.basket.BasketItem;
import ecomarkets.core.domain.core.fair.FairId;
import ecomarkets.core.domain.core.fair.FarmerProductAvailableInFair;
import ecomarkets.core.domain.core.product.ProductId;


@ApplicationScoped
public class DivideProductsToFarmers {

    /**
     * PRECONDITION: Ensure there is a unique constraint for the combination of fairId, farmerId, and productId fields.
     * This means, for each product produced by a farmer to a fair will have an amount.
     */
    public List<FarmerProductionToDeliver> process(FairId fairId){
        List<FarmerProductAvailableInFair> productsAmountInFair =  FarmerProductAvailableInFair.find("fairId", fairId).list();

        Map<ProductId, Integer> productByTotalAmountInBaskets = findProductAmountInBaskets(fairId);

        return divideProductionForEachFarmer(fairId, productByTotalAmountInBaskets, productsAmountInFair);
    }

    List<FarmerProductionToDeliver> divideProductionForEachFarmer(FairId fairId, Map<ProductId, Integer> productByTotalAmountInBaskets,
                                                                  List<FarmerProductAvailableInFair> productsAmountInFair) {
        List<FarmerProductionToDeliver> result = new ArrayList<>();

        for(Map.Entry<ProductId, Integer> productByTotalAmount : productByTotalAmountInBaskets.entrySet()){
            final ProductId productId = productByTotalAmount.getKey();
            final int sumAmountProductInBaskets = productByTotalAmount.getValue();

            List<FarmerProductAvailableInFair> farmersProductSupplyInDescendingAmountOrder = filterByProductInDescendingAmount(productId, productsAmountInFair);
            result.addAll(divideProductFromSmallerToGreaterFarmerStock(farmersProductSupplyInDescendingAmountOrder, sumAmountProductInBaskets));
        }

        return result;
    }

    private List<FarmerProductionToDeliver> divideProductFromSmallerToGreaterFarmerStock(List<FarmerProductAvailableInFair> productsInAscendingAmountOrder, final int sumAmountProductInBaskets) {
        List<FarmerProductionToDeliver> result = new ArrayList<>();

        final int qtFarmers = productsInAscendingAmountOrder.size();

        if(qtFarmers == 0){
            return result;
        }

        int qtToDeliverForEachFarmer = Math.ceilDiv(sumAmountProductInBaskets, qtFarmers);
        int totalDelivered = 0;
        int qtFarmersPendingToDeliver = 0;
        for(FarmerProductAvailableInFair item : productsInAscendingAmountOrder){
            final int amountAvailableInFairByFarmer = item.getAmount();

            final int amountToDeliver = calculateAmountToDeliverByFarmerToSupplyBaskets(sumAmountProductInBaskets, amountAvailableInFairByFarmer, qtToDeliverForEachFarmer, totalDelivered);

            if(basketsAlreadySupplied(amountToDeliver)){
                break;
            }

            int remainder = getAmountNotSuppliedByFarmer(amountAvailableInFairByFarmer, qtToDeliverForEachFarmer);

            qtFarmersPendingToDeliver--;

            qtToDeliverForEachFarmer = distributeRemainderBetweenPendingFarmers(qtFarmersPendingToDeliver, qtToDeliverForEachFarmer, remainder);

            result.add(FarmerProductionToDeliver.of(item.getFairId(),
                    item.getFarmerId(),
                    item.getProductId(),
                    amountToDeliver));

            totalDelivered += amountToDeliver;
        }

        notifyStockFaults(sumAmountProductInBaskets, totalDelivered);

        return result;
    }

    private boolean basketsAlreadySupplied(int amountToDeliver) {
        return amountToDeliver <= 0;
    }

    private int calculateAmountToDeliverByFarmerToSupplyBaskets(int sumAmountProductInBaskets, int amountAvailableInFairByFarmer, int qtToDeliverForEachFarmer, int totalDelivered) {
        int amountToDeliver;
        amountToDeliver = requiredAmountToDeliver(amountAvailableInFairByFarmer, qtToDeliverForEachFarmer);
        amountToDeliver = verifyAmountToDeliverSurpassAmountInBaskets(sumAmountProductInBaskets, totalDelivered, amountToDeliver);
        return amountToDeliver;
    }

    private void notifyStockFaults(int sumAmountProductInBaskets, int totalDelivered) {
        if(totalDelivered < sumAmountProductInBaskets){
            // TODO: Implement notification for reporting inconsistencies in production division among farmers caused by stock faults.
        }
    }

    private int distributeRemainderBetweenPendingFarmers(int qtFarmers, int qtToDeliverForEachFarmer, int remainder) {
        if(qtFarmers > 0){
            qtToDeliverForEachFarmer += Math.ceilDiv(remainder, qtFarmers);
        }
        return qtToDeliverForEachFarmer;
    }

    private int getAmountNotSuppliedByFarmer(int amountAvailableInFairByFarmer, int qtToDeliverForEachFarmer) {
        int remainder;
        if(hasFarmerAmountToSupplyBaskets(amountAvailableInFairByFarmer, qtToDeliverForEachFarmer)){
            remainder = 0;
        }else{
            remainder = qtToDeliverForEachFarmer - amountAvailableInFairByFarmer;
        }
        return remainder;
    }

    private int requiredAmountToDeliver(int amountAvailableInFairByFarmer, int qtToDeliverForEachFarmer) {
        int result;
        if(hasFarmerAmountToSupplyBaskets(amountAvailableInFairByFarmer, qtToDeliverForEachFarmer)){
            result = qtToDeliverForEachFarmer;
        }else{
            result = amountAvailableInFairByFarmer;
        }
        return result;
    }

    private boolean hasFarmerAmountToSupplyBaskets(int amountAvailableInFairByFarmer, int qtToDeliverForEachFarmer) {
        return amountAvailableInFairByFarmer >= qtToDeliverForEachFarmer;
    }

    private int verifyAmountToDeliverSurpassAmountInBaskets(int sumAmountProductInBaskets, int totalDelivered, int amountToDeliver) {
        if(totalDelivered + amountToDeliver > sumAmountProductInBaskets){
            amountToDeliver = sumAmountProductInBaskets - totalDelivered;
        }
        return amountToDeliver;
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

    private List<FarmerProductAvailableInFair> filterByProductInDescendingAmount(ProductId productId, List<FarmerProductAvailableInFair> productsAvailableInFair){
        return productsAvailableInFair.stream().filter(p -> p.getProductId().equals(productId)).sorted(Comparator.comparing(FarmerProductAvailableInFair::getAmount).reversed()).toList();
    }

}
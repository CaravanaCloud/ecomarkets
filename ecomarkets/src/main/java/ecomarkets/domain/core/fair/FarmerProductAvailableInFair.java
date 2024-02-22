package ecomarkets.domain.core.fair;

import com.google.errorprone.annotations.Immutable;
import ecomarkets.domain.core.farmer.FarmerId;
import ecomarkets.domain.core.product.ProductId;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedNativeQuery;

@Entity
@NamedNativeQuery(
        name = "FarmerProductAvailableInFair.amountProductAvailable",
        query = """
                    SELECT (stockSum - basketItemsSum) AS result
                      FROM (
                        SELECT SUM(item.amount) AS basketItemsSum
                          FROM Basket_items item
                          JOIN Basket b ON item.basket_id = b.id
                         WHERE item.product_id = :productId 
                           AND b.fair_id = :fairId ) AS basketItemsSum,
                        (
                        SELECT SUM(amount) AS stockSum
                          FROM FarmerProductAvailableInFair
                         WHERE product_id = :productId
                           and fair_id = :fairId ) AS stockSum
                """,
        resultClass = Double.class
)
// TODO: Add a unique constraint for the combination of fairId, farmerId, and productId fields.
@Immutable
public class FarmerProductAvailableInFair extends PanacheEntity{
    private FairId fairId;
    private FarmerId farmerId;
    private ProductId productId;
    private Integer amount;

    private FarmerProductAvailableInFair(){}

    public static FarmerProductAvailableInFair of(FairId fairId,
                                                  FarmerId farmerId,
                                                  ProductId productId,
                                                  Integer amount){
        FarmerProductAvailableInFair result = new FarmerProductAvailableInFair();
        result.farmerId = farmerId;
        result.productId = productId;
        result.amount = amount;
        result.fairId = fairId;
        return result;
    }

    public ProductId getProductId(){
        return this.productId;
    }

    public Integer getAmount(){
        return this.amount;
    }
    
    public FarmerId getFarmerId(){
        return this.farmerId;
    }

    public FairId getFairId(){
        return this.fairId;
    }

    public static Double getAmountProductAvailable(FairId fairId, ProductId productId){
        return (Double) getEntityManager().createNamedQuery("FarmerProductAvailableInFair.amountProductAvailable")
                .setParameter("fairId", fairId.id())
                .setParameter("productId", productId.id())
                .getSingleResult();
    }
}

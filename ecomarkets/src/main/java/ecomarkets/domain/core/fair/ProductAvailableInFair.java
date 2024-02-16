package ecomarkets.domain.core.fair;

import com.google.errorprone.annotations.Immutable;
import ecomarkets.domain.core.farmer.FarmerId;
import ecomarkets.domain.core.product.ProductId;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedNativeQuery;

@Entity
@NamedNativeQuery(
        name = "ProductStock.availableStock",
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
                          FROM ProductAvailableInFair
                         WHERE product_id = :productId
                           and fair_id = :fairId ) AS stockSum
                """,
        resultClass = Double.class
)
@Immutable
public class ProductAvailableInFair extends PanacheEntity{
    private FairId fairId;
    private FarmerId farmerId;
    private ProductId productId;
    private Integer amount;

    private ProductAvailableInFair(){}

    public static ProductAvailableInFair of(FairId fairId,
                                            FarmerId farmerId,
                                            ProductId productId,
                                            Integer amount){
        ProductAvailableInFair result = new ProductAvailableInFair();
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

    public static Double getAvailableStock(FairId fairId, ProductId productId){
        return (Double) getEntityManager().createNamedQuery("ProductStock.availableStock")
                .setParameter("fairId", fairId.id())
                .setParameter("productId", productId.id())
                .getSingleResult();
    }
}

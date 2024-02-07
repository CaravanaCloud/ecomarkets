package ecomarkets.domain.core.product;

import com.google.errorprone.annotations.Immutable;
import ecomarkets.domain.core.farmer.FarmerId;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedNativeQuery;

@Entity
@Immutable
//TODO this solution should be refined based on the domain
@NamedNativeQuery(
        name = "ProductStock.availableStock",
        query = """
                    SELECT (stockSum - basketItemsSum) AS result
                      FROM (
                        SELECT SUM(item.amount) AS basketItemsSum
                          FROM Basket_items item
                          JOIN Basket b ON item.basket_id = b.id
                         WHERE item.product_id = :productId ) AS basketItemsSum,
                        (
                        SELECT SUM(amount) AS stockSum
                          FROM ProductStock
                         WHERE product_id = :productId ) AS stockSum
                """,
        resultClass = Double.class
)
public class ProductStock extends PanacheEntity{
    private FarmerId farmerId;
    @ManyToOne
    private Product product;
    private Integer amount;

    private ProductStock(){}

    public static ProductStock of(FarmerId farmerId,
    Product product,
    Integer amount){
        ProductStock result = new ProductStock();
        result.farmerId = farmerId;
        result.product = product;
        result.amount = amount;
        return result;
    }

    //TODO this solution should be refined based on the domain
    public static Double getAvailableStock(ProductId productId){
        return (Double) getEntityManager().createNamedQuery("ProductStock.availableStock")
                .setParameter("productId", productId.id())
                .getSingleResult();
    }

    public Product getProduct(){
        return this.product;
    }

    public Integer getAmount(){
        return this.amount;
    }
    
    public FarmerId getFarmerId(){
        return this.farmerId;
    }
    
}

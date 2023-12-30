package ecomarkets.domain.core.product.image;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import org.hibernate.annotations.Immutable;

import java.util.ArrayList;
import java.util.List;

@Entity
@Immutable
public class ProductImage extends PanacheEntityBase {

    @Id
    @Column(name = "bucket_key")
    private String key;
    private String bucket;
    @Transient
    private List<Tag> tags;

    public ProductImage(String bucket, String key, List<Tag> tags){
        this(bucket, key);
        this.tags = tags;
    }
    public ProductImage(String bucket, String key){
        this.bucket = bucket;
        this.key = key;
        this.tags = new ArrayList<>();
    }
    private ProductImage(){
    }

    public static ProductImage of(String bucket,
                                  String key){
        return new ProductImage(bucket,
                key);
    }

    public ProductImage addTag(String key, String value){
        tags.add(new Tag(key, value));
        return this;
    }

    public String bucket(){
        return this.bucket;
    }

    public String key(){
        return this.key;
    }

    public List<Tag> tags(){
        return this.tags;
    }

}

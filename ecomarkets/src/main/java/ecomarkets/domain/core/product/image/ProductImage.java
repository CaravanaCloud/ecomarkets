package ecomarkets.domain.core.product.image;

import com.google.errorprone.annotations.Immutable;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Immutable
public class ProductImage extends PanacheEntity {
    private String bucket;
    @Column(name = "bucket_key")
    private String key;
    @ElementCollection
    private List<Tag> tags;

    public ProductImage(String bucket, String key, List<Tag> tags){
        this.bucket = bucket;
        this.key = key;
        this.tags = tags;
    }
    private ProductImage(){
    }

    public static ProductImage of(String bucket,
                                  String key,
                                  String product){
        List<Tag> tagList = new ArrayList<>();
        tagList.add(new Tag("product", product));
        return new ProductImage(bucket,
                key,
                tagList);
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

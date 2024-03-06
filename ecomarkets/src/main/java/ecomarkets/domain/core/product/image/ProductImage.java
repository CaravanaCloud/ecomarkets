package ecomarkets.domain.core.product.image;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import org.hibernate.annotations.Immutable;

import java.util.List;

@Entity
@Immutable
public class ProductImage extends PanacheEntityBase {

    @Id
    @Column(name = "bucket_key")
    private String key;
    private String bucket;
    private String mimetype;
    private String filename;
    @Transient
    private List<Tag> tags;

    ProductImage(String key, String bucket, String mimetype, String filename, List<Tag> tags) {
        this.key = key;
        this.bucket = bucket;
        this.mimetype = mimetype;
        this.filename = filename;
        this.tags = tags;
    }

    private ProductImage(){
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

    public String fileName(){ return this.filename; }

    public String mimeType(){ return this.mimetype; }

    public List<Tag> tags(){
        return this.tags;
    }

}

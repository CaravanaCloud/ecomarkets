package ecomarkets.core.domain.core.product.image;

import java.util.ArrayList;
import java.util.List;

public class ProductImageBuilder {

    private String key;
    private String bucket;

    private String mimeType;
    private String fileName;
    private List<Tag> tags;

    private ProductImageBuilder() {
        this.tags = new ArrayList<>();
    }

    public static ProductImageBuilder newInstance() {
        return new ProductImageBuilder();
    }

    public ProductImageBuilder withKey(String key) {
        this.key = key;
        return this;
    }
    public ProductImageBuilder withFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }
    public ProductImageBuilder withMimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public ProductImageBuilder withBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public ProductImageBuilder withTag(String key, String value) {
        this.tags.add(new Tag(key, value));
        return this;
    }

    public ProductImageBuilder addTag(String key, String value) {
        this.tags.add(new Tag(key, value));
        return this;
    }

    public ProductImage build() {
        if(bucket == null || key == null){
            throw new IllegalStateException("bucket and key should not be null");
        }
        ProductImage productImage = new ProductImage(key, bucket, mimeType, fileName, tags);
        return productImage;
    }
}


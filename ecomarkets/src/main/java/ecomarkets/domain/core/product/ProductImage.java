package ecomarkets.domain.core.product;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Embeddable
public record ProductImage (String bucket,
                            @Column(name = "bucket_key") String key,
                            @ElementCollection
                            Map<String, String> tags){
    public ProductImage{
        tags = Collections.unmodifiableMap(tags);
    }

    protected static ProductImage of(String bucket,
                                  String key,
                                  String product){
        Map<String, String> map = new HashMap<>();
        map.put("product", product);
        return new ProductImage(bucket,
                key,
                map);
    }
}

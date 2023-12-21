package ecomarkets.domain.core.product;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;

import java.util.Collection;
import java.util.Collections;

@Embeddable
public record ProductImage (@NotBlank String bucket,
                            @NotBlank String key,
                            Collection<String> tags){
    public ProductImage{
        tags = Collections.unmodifiableCollection(tags);
    }

    public static ProductImage of(String bucket,
                                  String key,
                                  Collection<String> tags){
        return new ProductImage(bucket,
                key,
                tags);
    }
}

package ecomarkets.domain.core.product.image;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record Tag (@Column(name = "tag_key") String key, String value){}

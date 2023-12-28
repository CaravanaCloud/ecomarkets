package ecomarkets.domain.core.product.image;

import java.nio.file.Path;

public interface ImageRepository {

    public void save(Path file,
                     ProductImage productImage);

    public String getBucketName();

}

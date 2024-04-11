package ecomarkets.core.domain.core.product.image;

import java.io.InputStream;
import java.nio.file.Path;

public interface ImageRepository {

    public void save(Path file,
                     ProductImage productImage);

    public void save(InputStream file, long contentLength,
                     ProductImage productImage);
    public byte [] find(ProductImage productImage);

    public void delete(ProductImage productImage);

    public String createPresignedGetUrl(ProductImage productImage);

    public String getBucketName();

}

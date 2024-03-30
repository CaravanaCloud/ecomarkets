package ecomarkets.core.domain.core.product.image;

import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import ecomarkets.core.domain.core.product.Product;
import ecomarkets.core.domain.core.product.image.ImageRepository;

import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class ProductImageTest {

    String fileName = "acerola.jpg";
    String mimetype = "image/jpeg";
    Path file = Paths.get("src/test/resources/ecomarkets/domain/core/product/acerola.jpg");

    @ConfigProperty(name = "bucket.name")
    String bucketName;

    Product product;

    @Inject
    ImageRepository imageRepository;

}

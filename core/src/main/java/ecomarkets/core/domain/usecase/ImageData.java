package ecomarkets.core.domain.usecase;

import java.io.File;
import java.io.InputStream;

public interface ImageData {
    File getFile();

    long getContentLength();

    String getFileName();

    String getMimeType();
}
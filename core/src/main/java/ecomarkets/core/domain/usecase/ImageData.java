package ecomarkets.core.domain.usecase;

import java.io.InputStream;

public interface ImageData {
    InputStream getFile();

    long getContentLength();

    String getFileName();

    String getMimeType();
}
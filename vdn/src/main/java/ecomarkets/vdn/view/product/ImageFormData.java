package ecomarkets.vdn.view.product;

import ecomarkets.core.domain.usecase.ImageData;

import java.io.InputStream;

public class ImageFormData implements ImageData {
    private InputStream file;
    private String fileName;
    private String mimeType;

    private long contentLength;

    public InputStream getFile() {
        return file;
    }

    public void setFile(InputStream file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }
}
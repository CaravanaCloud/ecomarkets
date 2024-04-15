package ecomarkets.vdn.view.product;


import ecomarkets.core.domain.usecase.product.ImageData;

import java.io.File;

public class ImageFormData implements ImageData {
    private File file;
    private String fileName;
    private String mimeType;

    private long contentLength;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
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
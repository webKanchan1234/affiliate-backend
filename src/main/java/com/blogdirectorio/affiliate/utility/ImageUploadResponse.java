package com.blogdirectorio.affiliate.utility;

public class ImageUploadResponse {

    private String url;
    private String fileId;

    public ImageUploadResponse(String url, String fileId) {
        this.url = url;
        this.fileId = fileId;
    }

    public String getUrl() {
        return url;
    }

    public String getFileId() {
        return fileId;
    }
}

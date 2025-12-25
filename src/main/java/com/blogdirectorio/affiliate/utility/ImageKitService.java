package com.blogdirectorio.affiliate.utility;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.models.FileCreateRequest;
import io.imagekit.sdk.models.results.Result;

@Service
public class ImageKitService {

    private final ImageKit imageKit;

    public ImageKitService(ImageKit imageKit) {
        this.imageKit = imageKit;
    }

    // ✅ UPLOAD
    public ImageUploadResponse uploadImage(MultipartFile file, String folder) {
        try {
            FileCreateRequest request = new FileCreateRequest(
                    file.getBytes(),
                    System.currentTimeMillis() + "_" + file.getOriginalFilename()
            );
            request.setFolder(folder);

            Result result = imageKit.upload(request);

            return new ImageUploadResponse(
                    result.getUrl(),
                    result.getFileId()
            );

        } catch (Exception e) {
            throw new RuntimeException("Image upload failed", e);
        }
    }

    // ✅ DELETE
    public void deleteImageByFileId(String fileId) {
        try {
            if (fileId != null && !fileId.isBlank()) {
                imageKit.deleteFile(fileId);
            }
        } catch (Exception e) {
            // log only, do not break main flow
            e.printStackTrace();
        }
    }
}
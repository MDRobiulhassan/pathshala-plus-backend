package com.example.pathshala_plus_backend.util;

import com.example.pathshala_plus_backend.exceptions.InvalidRequestException;
import lombok.experimental.UtilityClass;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@UtilityClass
public class ImageProcessingUtil {

    private static final int AVATAR_MAX_DIMENSION = 512;
    private static final double JPEG_QUALITY = 0.85;
    private static final long MAX_UPLOAD_BYTES = 2L * 1024 * 1024;

    public static byte[] resizeAvatar(MultipartFile file) throws IOException {
        validate(file);

        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            Thumbnails.of(file.getInputStream())
                    .size(AVATAR_MAX_DIMENSION, AVATAR_MAX_DIMENSION)
                    .outputFormat("jpg")
                    .outputQuality(JPEG_QUALITY)
                    .toOutputStream(output);
            return output.toByteArray();
        }
    }

    private static void validate(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidRequestException("File is empty");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new InvalidRequestException("File must be an image");
        }
        if (file.getSize() > MAX_UPLOAD_BYTES) {
            throw new InvalidRequestException("Image must be smaller than 2MB");
        }
    }
}
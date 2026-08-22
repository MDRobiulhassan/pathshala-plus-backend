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
    private static final double AVATAR_JPEG_QUALITY = 0.85;

    private static final int QUESTION_IMAGE_MAX_DIMENSION = 1600;
    private static final double QUESTION_JPEG_QUALITY = 0.9;

    private static final long MAX_UPLOAD_BYTES = 2L * 1024 * 1024;

    public static byte[] resizeAvatar(MultipartFile file) throws IOException {
        return resize(file, AVATAR_MAX_DIMENSION, "jpg", AVATAR_JPEG_QUALITY);
    }

    public static byte[] resizeQuestionImage(MultipartFile file) throws IOException {
        return resize(file, QUESTION_IMAGE_MAX_DIMENSION, "jpg", QUESTION_JPEG_QUALITY);
    }

    public static byte[] resize(MultipartFile file, int maxDimension, String outputFormat, double quality)
            throws IOException {
        validate(file);
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            Thumbnails.of(file.getInputStream())
                    .size(maxDimension, maxDimension)
                    .outputFormat(outputFormat)
                    .outputQuality(quality)
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
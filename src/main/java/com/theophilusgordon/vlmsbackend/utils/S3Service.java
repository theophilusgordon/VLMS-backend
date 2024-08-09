package com.theophilusgordon.vlmsbackend.utils;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.theophilusgordon.vlmsbackend.constants.ExceptionConstants;
import com.theophilusgordon.vlmsbackend.constants.Patterns;
import com.theophilusgordon.vlmsbackend.exception.UploadFailException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 s3Client;
    private final PasswordEncoder passwordEncoder;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private static final String S3_URL = "s3://vlms-storage/dev/";

    public String uploadEncodedImage(String encodedImage) {
        if (!StringUtils.hasText(encodedImage) || encodedImage.contains(S3_URL)) {
            return encodedImage;
        }

        String[] imageInfo = encodedImage.split("\\.");
        if (imageInfo.length != 2) {
            throw new IllegalArgumentException(ExceptionConstants.INVALID_IMAGE + encodedImage);
        }

        Matcher matcher = Pattern.compile(Patterns.ENCODED_IMAGE_INFO).matcher(encodedImage);
        if (!matcher.find()) {
            throw new IllegalArgumentException(ExceptionConstants.INVALID_IMAGE + encodedImage);
        }
        String contentType = matcher.group(1);
        byte[] imageBytes = decodeImage(imageInfo[0]);
        String key = generateKey(contentType);

        try (InputStream stream = new ByteArrayInputStream(imageBytes)) {
            s3Client.putObject(bucketName, key, stream, null);
        } catch (SdkClientException | IOException e) {
            throw new UploadFailException(ExceptionConstants.UPLOAD_FAIL + e.getMessage());
        }

        return s3Client.getUrl(bucketName, key).toString();
    }


    public void deleteImage(String imageUrl) {
        if (!StringUtils.hasText(imageUrl) || !imageUrl.contains(S3_URL)) {
            return;
        }
        String key = imageUrl.substring(S3_URL.length());
        s3Client.deleteObject(bucketName, key);
    }

    private byte[] decodeImage(String encodedImage) {
        return Base64.getUrlDecoder().decode(encodedImage);
    }

    private String generateKey(String contentType) {
        return passwordEncoder.encode(contentType + System.currentTimeMillis());
    }
}

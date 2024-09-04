package com.theophilusgordon.vlmsbackend.utils;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.theophilusgordon.vlmsbackend.constants.ExceptionConstants;
import com.theophilusgordon.vlmsbackend.exception.UploadFailException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 s3Client;
    private final PasswordEncoder passwordEncoder;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private static final String S3_URL = "https://vlms-storage.s3.amazonaws.com";

    public String uploadImage(MultipartFile profilePhoto) {


        String fileName = UUID.randomUUID() + "_" + profilePhoto.getOriginalFilename();
        String key = "dev/";

        try (InputStream stream = new ByteArrayInputStream(profilePhoto.getBytes())) {
            s3Client.putObject(bucketName, key + fileName, stream, null);
        } catch (SdkClientException | IOException e) {
            throw new UploadFailException(ExceptionConstants.UPLOAD_FAIL + e.getMessage());
        }

        return s3Client.getUrl(bucketName, key + fileName).toString();
    }


    public void deleteImage(String imageUrl) {
        if (!imageUrl.contains(S3_URL)) {
            return;
        }
        String fileName = imageUrl.substring(S3_URL.length());
        System.out.println("::::::::FIle Name::::::::::" + fileName);
        s3Client.deleteObject(bucketName, fileName);
    }
}

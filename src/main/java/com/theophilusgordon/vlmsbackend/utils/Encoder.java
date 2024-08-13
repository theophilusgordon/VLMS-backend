package com.theophilusgordon.vlmsbackend.utils;

import com.theophilusgordon.vlmsbackend.exception.BadRequestException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Encoder {
    public static String encodeImageToBase64(MultipartFile file) {
        try {
            String contentType = file.getContentType();
            byte[] bytes = file.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(bytes);
            return "data:" + contentType + ";base64," + base64Image;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new BadRequestException("Failed to encode image to Base64");
        }
    }
}

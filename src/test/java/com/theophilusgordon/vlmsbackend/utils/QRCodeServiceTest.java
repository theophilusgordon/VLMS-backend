package com.theophilusgordon.vlmsbackend.utils;

import com.google.zxing.WriterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QRCodeServiceTest {

    @InjectMocks
    private QRCodeService qrCodeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateQRCodeImage() throws WriterException, IOException {
        String qrCodeText = "Test QR Code";
        byte[] qrCodeImage = qrCodeService.generateQRCodeImage(qrCodeText);

        assertNotNull(qrCodeImage);
        assertTrue(qrCodeImage.length > 0);
    }
}


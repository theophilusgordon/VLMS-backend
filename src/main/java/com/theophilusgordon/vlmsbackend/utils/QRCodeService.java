package com.theophilusgordon.vlmsbackend.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class QRCodeService {

   public byte[] generateQRCodeImage(String barcodeText) {
    try {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 200, 200);

        BufferedImage qrCodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        ImageIO.write(qrCodeImage, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    } catch (WriterException | IOException e) {
        System.err.println("Could not generate QR code: " + e.getMessage());
        return new byte[0];
    }
}
}

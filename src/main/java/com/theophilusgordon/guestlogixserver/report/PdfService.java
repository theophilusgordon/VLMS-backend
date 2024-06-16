package com.theophilusgordon.guestlogixserver.report;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.theophilusgordon.guestlogixserver.visit.VisitService;
import com.theophilusgordon.guestlogixserver.guest.GuestService;
import com.theophilusgordon.guestlogixserver.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final GuestService guestService;
    private final UserService userService;
    private final VisitService visitService;

    public byte[] createPdfWithChart(byte[] chartImage, ReportGenerateRequest request) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);


        long totalGuests = guestService.getTotalGuests();
        int totalHosts = userService.getTotalHosts();
        int totalVisits = visitService.getTotalVisits();
        int totalCurrentGuests = visitService.getTotalCurrentGuests();
        int totalVisitsForPeriod = visitService.getTotalVisitsBetween(request.getStartDate(), request.getEndDate());

        document.add(new Paragraph("This is a sample PDF with a chart."));
        document.add(new Paragraph("Total Guests: " + totalGuests));
        document.add(new Paragraph("Total Hosts: " + totalHosts));
        document.add(new Paragraph("Total Visits: " + totalVisits));
        document.add(new Paragraph("Average Visits For Period: " + totalVisitsForPeriod));
        document.add(new Paragraph("Total Current Guests: " + totalCurrentGuests));

        ImageData imageData = ImageDataFactory.create(chartImage);
        Image pdfImg = new Image(imageData);
        document.add(pdfImg);

        document.close();
        return byteArrayOutputStream.toByteArray();
    }
}
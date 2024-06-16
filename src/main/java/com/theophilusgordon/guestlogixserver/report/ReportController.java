package com.theophilusgordon.guestlogixserver.report;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reports")
public class ReportController {
    private final PdfService pdfService;
    private ChartService chartService;

    @Operation(summary = "Generate PDF with chart", description = "Generate a PDF file with charts")
    @PostMapping()
    public ResponseEntity<byte[]> generatePdf(ReportGenerateRequest request) throws Exception {
        byte[] chartImage = chartService.createChart();
        byte[] pdfContent = pdfService.createPdfWithChart(chartImage, request);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "chart.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfContent);
    }
}

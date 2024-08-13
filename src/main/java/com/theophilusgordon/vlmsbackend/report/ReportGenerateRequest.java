package com.theophilusgordon.vlmsbackend.report;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ReportGenerateRequest {
    private String reportType;
    private String[] hostIds;
    private String[] guestIds;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}

package com.theophilusgordon.guestlogixserver.report;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReportGenerateRequest {
    private String reportType;
    private String[] hostIds;
    private String[] guestIds;
    private String startDate;
    private String endDate;
}

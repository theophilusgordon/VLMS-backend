package com.theophilusgordon.guestlogixserver.dashboard;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/statistics")
    public ResponseEntity<DashboardStatisticsResponse> getStatistics() {
        return ResponseEntity.ok(dashboardService.getStatistics());
    }
}

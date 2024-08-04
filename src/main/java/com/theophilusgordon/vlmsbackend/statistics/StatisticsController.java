package com.theophilusgordon.vlmsbackend.statistics;

import com.theophilusgordon.vlmsbackend.constants.AuthConstants;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/statistics")
public class StatisticsController {
    private final StatisticsService statisticsService;

    @Operation(summary = "Get statistics", description = "Get statistics")
    @PreAuthorize(AuthConstants.ADMIN_HOST_AUTHORIZATION)
    @GetMapping()
    public ResponseEntity<StatisticsResponse> getStatistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }
}

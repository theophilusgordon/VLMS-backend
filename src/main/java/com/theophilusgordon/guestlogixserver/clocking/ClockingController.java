package com.theophilusgordon.guestlogixserver.clocking;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/clocking")
public class ClockingController {
    private final ClockingService clockingService;

    @Operation(summary = "Clock in", description = "Clock in a host or admin")
    @PostMapping("/clock-in")
    public ResponseEntity<ClockingResponse> clockIn(ClockInRequest request) {
        return ResponseEntity.ok(clockingService.clockIn(request));
    }

    @Operation(summary = "Clock out", description = "Clock out a host or admin")
    @PostMapping("/clock-out")
    public ResponseEntity<ClockingResponse> clockOut(ClockOutRequest request) {
        return ResponseEntity.ok(clockingService.clockOut(request));
    }
}

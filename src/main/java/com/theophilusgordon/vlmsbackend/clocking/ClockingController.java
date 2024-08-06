package com.theophilusgordon.vlmsbackend.clocking;

import com.theophilusgordon.vlmsbackend.constants.AuthConstants;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/clocking")
public class ClockingController {
    private final ClockingService clockingService;

    @Operation(summary = "Clock in", description = "Clock in a host or admin")
    @PreAuthorize(AuthConstants.ADMIN_HOST_AUTHORIZATION)
    @PostMapping("/clock-in")
    public ResponseEntity<ClockingResponse> clockIn(@RequestBody @Valid ClockInRequest request) {
        return ResponseEntity.ok(clockingService.clockIn(request));
    }

    @Operation(summary = "Clock out", description = "Clock out a host or admin")
    @PreAuthorize(AuthConstants.ADMIN_HOST_AUTHORIZATION)
    @PostMapping("/clock-out")
    public ResponseEntity<ClockingResponse> clockOut(@RequestBody @Valid ClockOutRequest request) {
        System.out.println("We are getting here");
        return ResponseEntity.ok(clockingService.clockOut(request));
    }
}

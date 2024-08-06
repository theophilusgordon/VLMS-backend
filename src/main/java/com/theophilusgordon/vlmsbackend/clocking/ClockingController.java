package com.theophilusgordon.vlmsbackend.clocking;

import com.theophilusgordon.vlmsbackend.constants.AuthConstants;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/clocking")
public class ClockingController {
    private final ClockingService clockingService;

    @Operation(summary = "Clock in", description = "Clock in a host or admin")
    @PreAuthorize(AuthConstants.ADMIN_HOST_AUTHORIZATION)
    @PostMapping("/clock-in")
    public ResponseEntity<ClockingResponse> clockIn(@RequestBody @Valid ClockInRequest request, Principal principal) {
        return ResponseEntity.ok(clockingService.clockIn(request, principal));
    }

    @Operation(summary = "Clock out", description = "Clock out a host or admin")
    @PreAuthorize(AuthConstants.ADMIN_HOST_AUTHORIZATION)
    @PostMapping("/clock-out")
    public ResponseEntity<ClockingResponse> clockOut(@RequestBody @Valid ClockOutRequest request) {
        return ResponseEntity.ok(clockingService.clockOut(request));
    }

    @Operation(summary = "Get clockings", description = "Get all clockings")
    @PreAuthorize(AuthConstants.ADMIN_AUTHORIZATION)
    @GetMapping
    public ResponseEntity<Page<ClockingResponse>> getClockings(Pageable pageable) {
        return ResponseEntity.ok(clockingService.getClockings(pageable));
    }

    @Operation(summary = "Get clockings by user id", description = "Get all clockings by user id")
    @PreAuthorize(AuthConstants.ADMIN_AUTHORIZATION)
    @GetMapping("/{userId}")
    public ResponseEntity<Page<ClockingResponse>> getClockingsByUserId(@PathVariable String userId, Pageable pageable) {
        return ResponseEntity.ok(clockingService.getClockingsByUserId(userId, pageable));
    }

    @Operation(summary = "Get current user clocking", description = "Get current user clocking")
    @PreAuthorize(AuthConstants.ADMIN_HOST_AUTHORIZATION)
    @GetMapping("/me")
    public ResponseEntity<Page<ClockingResponse>> getCurrentUserClocking(Principal principal, Pageable pageable) {
        return ResponseEntity.ok(clockingService.getCurrentUserClocking(principal, pageable));
    }
}

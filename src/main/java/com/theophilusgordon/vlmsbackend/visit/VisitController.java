package com.theophilusgordon.vlmsbackend.visit;

import com.google.zxing.WriterException;
import com.theophilusgordon.vlmsbackend.constants.AuthConstants;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/visits")
public class VisitController {
    private final VisitService visitService;

    @Operation(summary = "Check-in a guest", description = "Check in a guest for visit")
    @PostMapping("/checkin")
    public ResponseEntity<VisitResponse> checkIn(@RequestBody VisitRequest request) throws IOException, WriterException, MessagingException {
        return ResponseEntity.ok(visitService.checkIn(request));
    }

    @Operation(summary = "Check-out a guest", description = "Check out a guest after visit")
    @PatchMapping("/checkout/{checkinId}")
    public ResponseEntity<VisitResponse> checkOut(@PathVariable Integer checkinId) {
        return ResponseEntity.ok(visitService.checkOut(checkinId));
    }

    @Operation(summary = "Get all visits", description = "Get all visits")
    @PreAuthorize(AuthConstants.ADMIN_HOST_AUTHORIZATION)
    @GetMapping
    public ResponseEntity<Iterable<VisitResponse>> getCheckIns(Pageable pageable) {
        return ResponseEntity.ok(visitService.getCheckIns(pageable));
    }

    @Operation(summary = "Get a visit", description = "Get a visit by ID")
    @PreAuthorize(AuthConstants.ADMIN_HOST_AUTHORIZATION)
    @GetMapping("/{id}")
    public ResponseEntity<VisitResponse> getCheckIn(@PathVariable Integer id) {
        return ResponseEntity.ok(visitService.getCheckIn(id));
    }

    @Operation(summary = "Get all current guests", description = "Get all guests that have signed in but not yet signed out")
    @PreAuthorize(AuthConstants.ADMIN_HOST_AUTHORIZATION)
    @GetMapping("/current-guests")
    public ResponseEntity<Iterable<VisitResponse>> getCurrentGuests() {
        return ResponseEntity.ok(visitService.getCurrentGuests());
    }

    @Operation(summary = "Get all visits by guest", description = "Get all visits for a guest by ID")
    @PreAuthorize(AuthConstants.ADMIN_HOST_AUTHORIZATION)
    @GetMapping("/guest/{guestId}")
    public ResponseEntity<Iterable<VisitResponse>> getCheckInsByGuest(@PathVariable UUID guestId) {
        return ResponseEntity.ok(visitService.getCheckInsByGuest(guestId));
    }

    @Operation(summary = "Get all guests by host", description = "Get all guests that have visited a host by host ID")
    @PreAuthorize(AuthConstants.ADMIN_HOST_AUTHORIZATION)
    @GetMapping("/host/{hostId}")
    public ResponseEntity<Iterable<VisitResponse>> getCheckInsByHost(@PathVariable UUID hostId) {
        return ResponseEntity.ok(visitService.getCheckInsByHost(hostId));
    }

    @Operation(summary = "Get all visits by check-in date", description = "Get all visits by check-in date")
    @PreAuthorize(AuthConstants.ADMIN_HOST_AUTHORIZATION)
    @GetMapping("/date/{checkInDate}")
    public ResponseEntity<Iterable<VisitResponse>> getCheckInsByCheckInDate(@PathVariable LocalDateTime checkInDate) {
        return ResponseEntity.ok(visitService.getCheckInsByCheckInDate(checkInDate));
    }

    @Operation(summary = "Get all visits for a period", description = "Get all visits for a period by start and end date")
    @PreAuthorize(AuthConstants.ADMIN_HOST_AUTHORIZATION)
    @GetMapping("/period/{start}/{end}")
    public ResponseEntity<Iterable<VisitResponse>> getCheckInsByPeriod(@PathVariable LocalDateTime start, @PathVariable LocalDateTime end) {
        return ResponseEntity.ok(visitService.getCheckInsByPeriod(start, end));
    }

    @Operation(summary = "Get all visits for a host in a period", description = "Get all visits for a host in a period by host ID, start and end date")
    @PreAuthorize(AuthConstants.ADMIN_HOST_AUTHORIZATION)
    @GetMapping("/host-period/{hostId}/{start}/{end}")
    public ResponseEntity<Iterable<VisitResponse>> getCheckInsByHostPeriod(@PathVariable UUID hostId, @PathVariable LocalDateTime start, @PathVariable LocalDateTime end) {
        return ResponseEntity.ok(visitService.getCheckInsByHostAndPeriod(hostId, start, end));
    }
}

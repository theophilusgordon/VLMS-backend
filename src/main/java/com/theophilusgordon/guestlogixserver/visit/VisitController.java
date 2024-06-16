package com.theophilusgordon.guestlogixserver.visit;

import com.google.zxing.WriterException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/visits")
public class VisitController {
    private final VisitService visitService;

    @Operation(summary = "Check-in a guest", description = "Check in a guest for visit")
    @PostMapping("/checkin")
    public ResponseEntity<VisitServiceResponse> checkIn(@RequestBody VisitRequest request) throws IOException, WriterException, MessagingException {
        return ResponseEntity.ok(visitService.checkIn(request));
    }

    @Operation(summary = "Check-out a guest", description = "Check out a guest after visit")
    @PutMapping("/checkout/{checkinId}")
    public ResponseEntity<VisitServiceResponse> checkOut(@PathVariable Integer checkinId) {
        return ResponseEntity.ok(visitService.checkOut(checkinId));
    }

    @Operation(summary = "Get all visits", description = "Get all visits")
    @GetMapping
    public ResponseEntity<Iterable<VisitServiceResponse>> getCheckIns() {
        return ResponseEntity.ok(visitService.getCheckIns());
    }

    @Operation(summary = "Get a visit", description = "Get a visit by ID")
    @GetMapping("/{id}")
    public ResponseEntity<VisitServiceResponse> getCheckIn(@PathVariable Integer id) {
        return ResponseEntity.ok(visitService.getCheckIn(id));
    }

    @Operation(summary = "Get all current guests", description = "Get all guests that have signed in but not yet signed out")
    @GetMapping("/current-guests")
    public ResponseEntity<Iterable<VisitServiceResponse>> getCurrentGuests() {
        return ResponseEntity.ok(visitService.getCurrentGuests());
    }

    @Operation(summary = "Get all visits by guest", description = "Get all visits for a guest by ID")
    @GetMapping("/guest/{guestId}")
    public ResponseEntity<Iterable<VisitServiceResponse>> getCheckInsByGuest(@PathVariable String guestId) {
        return ResponseEntity.ok(visitService.getCheckInsByGuest(guestId));
    }

    @Operation(summary = "Get all guests by host", description = "Get all guests that have visited a host by host ID")
    @GetMapping("/host/{hostId}")
    public ResponseEntity<Iterable<VisitServiceResponse>> getCheckInsByHost(@PathVariable String hostId) {
        return ResponseEntity.ok(visitService.getCheckInsByHost(hostId));
    }

    @Operation(summary = "Get all visits by check-in date", description = "Get all visits by check-in date")
    @GetMapping("/date/{checkInDate}")
    public ResponseEntity<Iterable<VisitServiceResponse>> getCheckInsByCheckInDate(@PathVariable String checkInDate) {
        return ResponseEntity.ok(visitService.getCheckInsByCheckInDate(checkInDate));
    }

    @Operation(summary = "Get all visits for a period", description = "Get all visits for a period by start and end date")
    @GetMapping("/period/{start}/{end}")
    public ResponseEntity<Iterable<VisitServiceResponse>> getCheckInsByPeriod(@PathVariable String start, @PathVariable String end) {
        return ResponseEntity.ok(visitService.getCheckInsByPeriod(start, end));
    }

    @Operation(summary = "Get all visits for a host in a period", description = "Get all visits for a host in a period by host ID, start and end date")
    @GetMapping("/host-period/{hostId}/{start}/{end}")
    public ResponseEntity<Iterable<VisitServiceResponse>> getCheckInsByHostPeriod(@PathVariable String hostId, @PathVariable String start, @PathVariable String end) {
        return ResponseEntity.ok(visitService.getCheckInsByHostAndPeriod(hostId, start, end));
    }
}

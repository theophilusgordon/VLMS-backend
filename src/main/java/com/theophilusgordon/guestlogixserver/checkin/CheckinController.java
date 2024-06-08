package com.theophilusgordon.guestlogixserver.checkin;

import com.google.zxing.WriterException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/checkin")
public class CheckinController {
    private final CheckinService checkinService;

    @Operation(summary = "Check-in a guest", description = "Check in a guest for visit")
    @PostMapping
    public ResponseEntity<CheckinResponse> checkIn(@RequestBody CheckinRequest request) throws IOException, WriterException, MessagingException {
        return ResponseEntity.ok(checkinService.checkIn(request));
    }

    @Operation(summary = "Check-out a guest", description = "Check out a guest after visit")
    @PutMapping("/checkout/{id}")
    public ResponseEntity<CheckinResponse> checkOut(@PathVariable Integer id) {
        return ResponseEntity.ok(checkinService.checkOut(id));
    }

    @Operation(summary = "Get all check-ins", description = "Get all check-ins")
    @GetMapping
    public ResponseEntity<Iterable<CheckinResponse>> getCheckIns() {
        return ResponseEntity.ok(checkinService.getCheckIns());
    }

    @Operation(summary = "Get a check-in", description = "Get a check-in by ID")
    @GetMapping("/{id}")
    public ResponseEntity<CheckinResponse> getCheckIn(@PathVariable Integer id) {
        return ResponseEntity.ok(checkinService.getCheckIn(id));
    }

    @Operation(summary = "Get all current guests", description = "Get all guests that have signed in but not yet signed out")
    @GetMapping("/current-guests")
    public ResponseEntity<Iterable<CheckinResponse>> getCurrentGuests() {
        return ResponseEntity.ok(checkinService.getCurrentGuests());
    }

    @Operation(summary = "Get all check-ins by guest", description = "Get all check-ins for a guest by ID")
    @GetMapping("/guest/{guestId}")
    public ResponseEntity<Iterable<CheckinResponse>> getCheckInsByGuest(@PathVariable String guestId) {
        return ResponseEntity.ok(checkinService.getCheckInsByGuest(guestId));
    }

    @Operation(summary = "Get all guests by host", description = "Get all guests that have visited a host by host ID")
    @GetMapping("/host/{hostId}")
    public ResponseEntity<Iterable<CheckinResponse>> getCheckInsByHost(@PathVariable String hostId) {
        return ResponseEntity.ok(checkinService.getCheckInsByHost(hostId));
    }

    @Operation(summary = "Get all check-ins by check-in date", description = "Get all check-ins by check-in date")
    @GetMapping("/date/{checkIn}")
    public ResponseEntity<Iterable<CheckinResponse>> getCheckInsByCheckInDate(@PathVariable String checkIn) {
        return ResponseEntity.ok(checkinService.getCheckInsByCheckInDate(checkIn));
    }

    @Operation(summary = "Get all check-ins for a period", description = "Get all check-ins for a period by start and end date")
    @GetMapping("/period/{start}/{end}")
    public ResponseEntity<Iterable<CheckinResponse>> getCheckInsByPeriod(@PathVariable String start, @PathVariable String end) {
        return ResponseEntity.ok(checkinService.getCheckInsByPeriod(start, end));
    }

    @Operation(summary = "Get all check-ins for a host in a period", description = "Get all check-ins for a host in a period by host ID, start and end date")
    @GetMapping("/host-period/{hostId}/{start}/{end}")
    public ResponseEntity<Iterable<CheckinResponse>> getCheckInsByHostPeriod(@PathVariable String hostId, @PathVariable String start, @PathVariable String end) {
        return ResponseEntity.ok(checkinService.getCheckInsByHostAndPeriod(hostId, start, end));
    }
}

package com.theophilusgordon.guestlogixserver.checkIn;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/check-in")
public class CheckInController {
    private final CheckInService checkInService;

    @PostMapping
    public ResponseEntity<CheckInResponse> checkIn(@RequestBody CheckInRequest request) {
        return ResponseEntity.ok(checkInService.checkIn(request));
    }

    @PutMapping("/check-out/{id}")
    public ResponseEntity<CheckInResponse> checkOut(@PathVariable Integer id) {
        return ResponseEntity.ok(checkInService.checkOut(id));
    }

    @GetMapping
    public ResponseEntity<Iterable<CheckInResponse>> getCheckIns() {
        return ResponseEntity.ok(checkInService.getCheckIns());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CheckInResponse> getCheckIn(@PathVariable Integer id) {
        return ResponseEntity.ok(checkInService.getCheckIn(id));
    }

    @GetMapping("/guest/{guestId}")
    public ResponseEntity<Iterable<CheckInResponse>> getCheckInsByGuest(@PathVariable String guestId) {
        return ResponseEntity.ok(checkInService.getCheckInsByGuest(guestId));
    }

    @GetMapping("/host/{hostId}")
    public ResponseEntity<Iterable<CheckInResponse>> getCheckInsByHost(@PathVariable String hostId) {
        return ResponseEntity.ok(checkInService.getCheckInsByHost(hostId));
    }

    @GetMapping("/check-in-day/{checkIn}")
    public ResponseEntity<Iterable<CheckInResponse>> getCheckInsByCheckInDate(@PathVariable String checkIn) {
        return ResponseEntity.ok(checkInService.getCheckInsByCheckInDate(checkIn));
    }

    @GetMapping("/period/{start}/{end}")
    public ResponseEntity<Iterable<CheckInResponse>> getCheckInsByPeriod(@PathVariable String start, @PathVariable String end) {
        return ResponseEntity.ok(checkInService.getCheckInsByPeriod(start, end));
    }

    @GetMapping("/host-period/{hostId}/{start}/{end}")
    public ResponseEntity<Iterable<CheckInResponse>> getCheckInsByHostPeriod(@PathVariable String hostId, @PathVariable String start, @PathVariable String end) {
        return ResponseEntity.ok(checkInService.getCheckInsByHostAndPeriod(hostId, start, end));
    }
}

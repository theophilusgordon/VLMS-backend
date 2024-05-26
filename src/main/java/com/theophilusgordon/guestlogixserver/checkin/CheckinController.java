package com.theophilusgordon.guestlogixserver.checkin;

import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/checkin")
public class CheckinController {
    private final CheckinService checkinService;

    @PostMapping
    public ResponseEntity<CheckinResponse> checkIn(@RequestBody CheckinRequest request) throws IOException, WriterException {
        return ResponseEntity.ok(checkinService.checkIn(request));
    }

    @PutMapping("/checkout/{id}")
    public ResponseEntity<CheckinResponse> checkOut(@PathVariable Integer id) {
        return ResponseEntity.ok(checkinService.checkOut(id));
    }

    @GetMapping
    public ResponseEntity<Iterable<CheckinResponse>> getCheckIns() {
        return ResponseEntity.ok(checkinService.getCheckIns());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CheckinResponse> getCheckIn(@PathVariable Integer id) {
        return ResponseEntity.ok(checkinService.getCheckIn(id));
    }

    @GetMapping("/guest/{guestId}")
    public ResponseEntity<Iterable<CheckinResponse>> getCheckInsByGuest(@PathVariable String guestId) {
        return ResponseEntity.ok(checkinService.getCheckInsByGuest(guestId));
    }

    @GetMapping("/host/{hostId}")
    public ResponseEntity<Iterable<CheckinResponse>> getCheckInsByHost(@PathVariable String hostId) {
        return ResponseEntity.ok(checkinService.getCheckInsByHost(hostId));
    }

    @GetMapping("/date/{checkIn}")
    public ResponseEntity<Iterable<CheckinResponse>> getCheckInsByCheckInDate(@PathVariable String checkIn) {
        return ResponseEntity.ok(checkinService.getCheckInsByCheckInDate(checkIn));
    }

    @GetMapping("/period/{start}/{end}")
    public ResponseEntity<Iterable<CheckinResponse>> getCheckInsByPeriod(@PathVariable String start, @PathVariable String end) {
        return ResponseEntity.ok(checkinService.getCheckInsByPeriod(start, end));
    }

    @GetMapping("/host-period/{hostId}/{start}/{end}")
    public ResponseEntity<Iterable<CheckinResponse>> getCheckInsByHostPeriod(@PathVariable String hostId, @PathVariable String start, @PathVariable String end) {
        return ResponseEntity.ok(checkinService.getCheckInsByHostAndPeriod(hostId, start, end));
    }
}

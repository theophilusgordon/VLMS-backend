package com.theophilusgordon.guestlogixserver.checkInOut;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/check-in-out")
public class CheckInOutController {
    private final CheckInOutService service;

    @PostMapping("/check-in")
    public ResponseEntity<?> checkIn(@RequestBody CheckInRequest request) {
        service.checkIn(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/check-out/{id}")
    public ResponseEntity<?> checkOut(@PathVariable Integer id) {
        service.checkOut(id);
        return ResponseEntity.ok().build();
    }
}

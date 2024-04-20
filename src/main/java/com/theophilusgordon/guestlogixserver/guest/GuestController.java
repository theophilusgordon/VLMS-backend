package com.theophilusgordon.guestlogixserver.guest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/guests")
public class GuestController {
    private final GuestService service;

    @PostMapping
    public ResponseEntity<GuestResponse> registerGuest(@RequestBody GuestRegisterRequest request) {
        return ResponseEntity.ok(service.registerGuest(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GuestResponse> updateGuest(@PathVariable String id, @RequestBody GuestUpdateRequest request) {
        return ResponseEntity.ok(service.updateGuest(id, request));
    }

    @GetMapping
    public ResponseEntity<Iterable<GuestResponse>> getAllGuests() {
        return ResponseEntity.ok(service.getAllGuests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuestResponse> getGuest(@PathVariable String id) {
        return ResponseEntity.ok(service.getGuest(id));
    }

    @DeleteMapping("/{id}")
    public void deleteGuest(@PathVariable String id) {
        service.deleteGuest(id);
    }
}

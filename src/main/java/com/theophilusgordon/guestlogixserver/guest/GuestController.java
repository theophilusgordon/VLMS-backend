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
    public ResponseEntity<Guest> registerGuest(@RequestBody GuestRegisterRequest request) {
        service.registerGuest(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Guest> updateGuest(@PathVariable String id, @RequestBody GuestUpdateRequest request) {
        service.updateGuest(id, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Iterable<Guest>> getAllGuests() {
        return ResponseEntity.ok(service.getAllGuests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Guest> getGuest(@PathVariable String id) {
        return ResponseEntity.ok(service.getGuest(id));
    }

    @DeleteMapping("/{id}")
    public void deleteGuest(@PathVariable String id) {
        service.deleteGuest(id);
    }
}

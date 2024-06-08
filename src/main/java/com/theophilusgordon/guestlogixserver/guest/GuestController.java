package com.theophilusgordon.guestlogixserver.guest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/guests")
public class GuestController {
    private final GuestService service;

    @Operation(summary = "Register a guest", description = "Register a guest")
    @PostMapping
    public ResponseEntity<GuestResponse> registerGuest(@RequestBody GuestRegisterRequest request) {
        return ResponseEntity.ok(service.registerGuest(request));
    }

    @Operation(summary = "Update a guest", description = "Update guest information")
    @PutMapping("/{id}")
    public ResponseEntity<GuestResponse> updateGuest(@PathVariable String id, @RequestBody GuestUpdateRequest request) {
        return ResponseEntity.ok(service.updateGuest(id, request));
    }

    @Operation(summary = "Get all guests", description = "Get all guests")
    @GetMapping
    public ResponseEntity<Iterable<GuestResponse>> getAllGuests() {
        return ResponseEntity.ok(service.getAllGuests());
    }

    @Operation(summary = "Get a guest", description = "Get a guest by ID")
    @GetMapping("/{id}")
    public ResponseEntity<GuestResponse> getGuest(@PathVariable String id) {
        return ResponseEntity.ok(service.getGuest(id));
    }
}

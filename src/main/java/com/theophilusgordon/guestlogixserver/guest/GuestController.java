package com.theophilusgordon.guestlogixserver.guest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/guests")
public class GuestController {
    private final GuestService service;
    private final GuestService guestService;

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

    @Operation(summary = "Get all guests", description = "Get all guests paginated. Default page is 1, default size is 10")
    @GetMapping
    public ResponseEntity<Page<GuestResponse>> getGuests(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String sort

    ) {
        Page<GuestResponse> guests = guestService.getGuests(page, size, sort);
        return ResponseEntity.ok(guests);
    }

    @Operation(summary = "Search guests", description = "Search guests by full name")
    @GetMapping("/search")
    public ResponseEntity<Page<GuestResponse>> searchGuests(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String query,
            @RequestParam String sort
    ){
        Page<GuestResponse> guests = guestService.searchGuests(query, page, size, sort);
        return ResponseEntity.ok(guests);
    }

    @Operation(summary = "Get a guest", description = "Get a guest by ID")
    @GetMapping("/{id}")
    public ResponseEntity<GuestResponse> getGuest(@PathVariable String id) {
        return ResponseEntity.ok(service.getGuest(id));
    }
}

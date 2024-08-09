package com.theophilusgordon.vlmsbackend.guest;

import com.theophilusgordon.vlmsbackend.constants.AuthConstants;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/guests")
public class GuestController {
    private final GuestService service;
    private final GuestService guestService;

    @Operation(summary = "Register a guest", description = "Register a guest")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Guest> registerGuest(
            @ModelAttribute @Valid GuestRegisterRequest request,
            @RequestParam("profilePhoto") MultipartFile profilePhoto) {
        return ResponseEntity.ok(service.registerGuest(request, profilePhoto));
    }

    @Operation(summary = "Get all guests", description = "Get all guests paginated. Default page is 0, default size is 10")
    @PreAuthorize(AuthConstants.ADMIN_AUTHORIZATION)
    @GetMapping
    public ResponseEntity<Page<Guest>> getGuests(Pageable pageable) {
        Page<Guest> guests = guestService.getGuests(pageable);
        return ResponseEntity.ok(guests);
    }

    @Operation(summary = "Search guests", description = "Search guests by full name")
    @PreAuthorize(AuthConstants.ADMIN_AUTHORIZATION)
    @GetMapping("/search")
    public ResponseEntity<Page<Guest>> searchGuests(Pageable pageable, @RequestParam String query){
        Page<Guest> guests = guestService.searchGuests(pageable, query);
        return ResponseEntity.ok(guests);
    }

    @Operation(summary = "Get a guest", description = "Get a guest by ID")
    @PreAuthorize(AuthConstants.ADMIN_AUTHORIZATION)
    @GetMapping("/{id}")
    public ResponseEntity<Guest> getGuest(@PathVariable String id) {
        return ResponseEntity.ok(service.getGuest(id));
    }
}

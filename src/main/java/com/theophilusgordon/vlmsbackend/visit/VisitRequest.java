package com.theophilusgordon.vlmsbackend.visit;

import jakarta.validation.constraints.NotBlank;

public record VisitRequest(
        @NotBlank(message = "Guest id is required")
        String guestId,
        @NotBlank(message  = "Host id is required")
        String hostId
) {
}

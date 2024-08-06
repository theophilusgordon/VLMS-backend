package com.theophilusgordon.vlmsbackend.clocking;

import jakarta.validation.constraints.NotBlank;

public record ClockInRequest(
        @NotBlank(message = "Work location is required")
        String workLocation
) {
}

package com.theophilusgordon.vlmsbackend.clocking;

import jakarta.validation.constraints.NotBlank;

public record ClockOutRequest(
        @NotBlank(message = "Clocking id is required")
        Integer clockingId
) {
}

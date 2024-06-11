package com.theophilusgordon.guestlogixserver.clocking;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ClockInRequest {
    @NotBlank(message = "User id is required")
    private String userId;
}

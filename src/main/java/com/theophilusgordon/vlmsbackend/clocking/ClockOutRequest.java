package com.theophilusgordon.vlmsbackend.clocking;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ClockOutRequest {
    @NotBlank(message = "Clocking id is required")
    private Integer clockingId;
}

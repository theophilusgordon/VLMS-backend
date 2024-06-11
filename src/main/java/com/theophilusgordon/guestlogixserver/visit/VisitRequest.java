package com.theophilusgordon.guestlogixserver.visit;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitRequest {
    @NotBlank(message = "Guest id is required")
    private String guestId;
    @NotBlank(message  = "Host id is required")
    private String hostId;
}

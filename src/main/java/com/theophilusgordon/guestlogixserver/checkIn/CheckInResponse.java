package com.theophilusgordon.guestlogixserver.checkIn;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.theophilusgordon.guestlogixserver.guest.GuestResponse;
import com.theophilusgordon.guestlogixserver.user.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInResponse {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("checkInDateTime")
    private LocalDateTime checkInDateTime;
    @JsonProperty("checkOutDateTime")
    private LocalDateTime checkOutDateTime;
    private GuestResponse guest;
    private UserResponse host;
}

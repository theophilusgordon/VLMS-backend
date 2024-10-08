package com.theophilusgordon.vlmsbackend.visit;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.theophilusgordon.vlmsbackend.guest.Guest;
import com.theophilusgordon.vlmsbackend.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitResponse {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("checkInDateTime")
    private LocalDateTime checkInDateTime;
    @JsonProperty("checkOutDateTime")
    private LocalDateTime checkOutDateTime;
    private Guest guest;
    private User host;
}

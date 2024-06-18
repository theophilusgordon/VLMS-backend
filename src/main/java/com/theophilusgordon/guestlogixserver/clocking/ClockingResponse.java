package com.theophilusgordon.guestlogixserver.clocking;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.theophilusgordon.guestlogixserver.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ClockingResponse {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("User")
    private User user;
    @JsonProperty("workLocation")
    private String workLocation;
    @JsonProperty("clockInDateTime")
    private String clockInDateTime;
    @JsonProperty("clockOutDateTime")
    private String clockOutDateTime;
}

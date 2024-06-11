package com.theophilusgordon.guestlogixserver.clocking;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ClockingResponse {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("workLocation")
    private String workLocation;
    @JsonProperty("clockInDateTime")
    private String clockInDateTime;
    @JsonProperty("clockOutDateTime")
    private String clockOutDateTime;
}

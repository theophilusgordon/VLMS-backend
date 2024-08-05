package com.theophilusgordon.vlmsbackend.clocking;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.theophilusgordon.vlmsbackend.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ClockingsResponse {
    @JsonProperty("User")
    private User user;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("workLocation")
    private String workLocation;
    @JsonProperty("clockInDateTime")
    private String clockInDateTime;
    @JsonProperty("clockOutDateTime")
    private String clockOutDateTime;
}
package com.theophilusgordon.guestlogixserver.clocking;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ClockingResponse {
    private Integer id;
    private String userId;
    private String workLocation;
    private String clockInDateTime;
    private String clockOutDateTime;
}

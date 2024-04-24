package com.theophilusgordon.guestlogixserver.checkin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckinRequest {
    private String guestId;
    private String hostId;
}

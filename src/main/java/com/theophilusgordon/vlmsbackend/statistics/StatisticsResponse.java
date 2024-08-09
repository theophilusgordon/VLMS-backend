package com.theophilusgordon.vlmsbackend.statistics;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsResponse {
    private long totalHosts;
    private long totalGuests;
    private long currentGuests;
    private long totalCheckins;
    private long totalCheckinsByDay;
    private long totalCheckinsByWeek;
    private long totalCheckinsByMonth;
    private long totalCheckinsByYear;
    private long totalCheckinsByCustomDateRange;
    private long totalCheckinsByHost;
    private long totalCheckinsByGuest;
    private long totalCheckinsByHostAndGuest;
    private long totalCheckinsByHostAndCustomDateRange;
    private long totalCheckinsByGuestAndCustomDateRange;
    private long totalCheckinsByHostAndGuestAndCustomDateRange;
}

package com.theophilusgordon.guestlogixserver.statistics;

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
    private double averageCheckinsPerDay;
    private double averageCheckinsPerWeek;
    private double averageCheckinsPerMonth;
    private double averageCheckinsPerYear;
    private double averageCheckinsPerCustomDateRange;
    private double averageCheckinsPerHost;
    private double averageCheckinsPerGuest;
    private double averageCheckinsPerHostAndGuest;
    private double averageCheckinsPerHostAndCustomDateRange;
    private double averageCheckinsPerGuestAndCustomDateRange;
    private double averageCheckinsPerHostAndGuestAndCustomDateRange;
}

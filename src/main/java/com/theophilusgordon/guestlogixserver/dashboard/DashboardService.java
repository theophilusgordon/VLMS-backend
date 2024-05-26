package com.theophilusgordon.guestlogixserver.dashboard;

import com.theophilusgordon.guestlogixserver.checkin.CheckinRepository;
import com.theophilusgordon.guestlogixserver.guest.GuestRepository;
import com.theophilusgordon.guestlogixserver.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final CheckinRepository checkinRepository;
    private final UserRepository hostRepository;
    private final GuestRepository guestRepository;

    public DashboardStatisticsResponse getStatistics() {
        long totalHosts = hostRepository.count();
        long totalGuests = guestRepository.count();
        long currentGuests = checkinRepository.countByCheckInDateTimeIsNotNullAndCheckOutDateTimeIsNull();
        long totalCheckins = checkinRepository.count();
        long totalCheckinsByDay = checkinRepository.countByCheckInDateTimeEquals(LocalDateTime.now());
        LocalDateTime startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime endOfWeek = LocalDate.now().with(DayOfWeek.SUNDAY).atTime(23, 59, 59);
        long totalCheckinsByWeek = checkinRepository.countByCheckinDateBetweenThisWeek(startOfWeek, endOfWeek);

        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).atTime(23, 59, 59);
        long totalCheckinsByMonth = checkinRepository.countByCheckinDateBetweenThisMonth(startOfMonth, endOfMonth);

        long totalCheckinsByYear = checkinRepository.countByCheckinDateBetweenThisYear();

        double averageCheckinsPerDay = (double) totalCheckins / 365;
        double averageCheckinsPerWeek = (double) totalCheckins / 52;
        double averageCheckinsPerMonth = (double) totalCheckins / 12;

        return DashboardStatisticsResponse.builder()
                .totalHosts(totalHosts)
                .totalGuests(totalGuests)
                .currentGuests(currentGuests)
                .totalCheckins(totalCheckins)
                .totalCheckinsByDay(totalCheckinsByDay)
                .totalCheckinsByWeek(totalCheckinsByWeek)
                .totalCheckinsByMonth(totalCheckinsByMonth)
                .totalCheckinsByYear(totalCheckinsByYear)
                .averageCheckinsPerDay(averageCheckinsPerDay)
                .averageCheckinsPerWeek(averageCheckinsPerWeek)
                .averageCheckinsPerMonth(averageCheckinsPerMonth)
                .averageCheckinsPerYear((double) totalCheckins)
                .build();
    }

}

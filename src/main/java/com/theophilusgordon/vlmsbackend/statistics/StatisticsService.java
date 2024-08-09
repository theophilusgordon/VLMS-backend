package com.theophilusgordon.vlmsbackend.statistics;

import com.theophilusgordon.vlmsbackend.visit.VisitRepository;
import com.theophilusgordon.vlmsbackend.guest.GuestRepository;
import com.theophilusgordon.vlmsbackend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final VisitRepository visitRepository;
    private final UserRepository hostRepository;
    private final GuestRepository guestRepository;

    public StatisticsResponse getStatistics() {
        long totalHosts = hostRepository.count();
        long totalGuests = guestRepository.count();
        long currentGuests = visitRepository.countByCheckInDateTimeIsNotNullAndCheckOutDateTimeIsNull();
        long totalCheckins = visitRepository.count();
        long totalCheckinsByDay = visitRepository.countByCheckInDateTimeEquals(LocalDateTime.now());
        LocalDateTime startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime endOfWeek = LocalDate.now().with(DayOfWeek.SUNDAY).atTime(23, 59, 59);
        long totalCheckinsByWeek = visitRepository.countByCheckinDateBetweenThisWeek(startOfWeek, endOfWeek);

        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).atTime(23, 59, 59);
        long totalCheckinsByMonth = visitRepository.countByCheckinDateBetweenThisMonth(startOfMonth, endOfMonth);

        long totalCheckinsByYear = visitRepository.countByCheckinDateBetweenThisYear();

        return StatisticsResponse.builder()
                .totalHosts(totalHosts)
                .totalGuests(totalGuests)
                .currentGuests(currentGuests)
                .totalCheckins(totalCheckins)
                .totalCheckinsByDay(totalCheckinsByDay)
                .totalCheckinsByWeek(totalCheckinsByWeek)
                .totalCheckinsByMonth(totalCheckinsByMonth)
                .totalCheckinsByYear(totalCheckinsByYear)
                .build();
    }
}

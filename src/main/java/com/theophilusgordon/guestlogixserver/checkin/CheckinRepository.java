package com.theophilusgordon.guestlogixserver.checkin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface CheckinRepository extends JpaRepository<Checkin, Integer> {
    Iterable<Checkin> findByGuestId(String guestId);
    Iterable<Checkin> findByHostId(String hostId);
    
    Iterable<Checkin> findByCheckInDateTime(LocalDateTime checkIn);

    Iterable<Checkin> findByCheckInDateTimeBetween(LocalDateTime start, LocalDateTime end);

    Iterable<Checkin> findByHostIdAndCheckInDateTimeBetween(String hostId, LocalDateTime start, LocalDateTime end);

    Long countByCheckInDateTimeEquals(LocalDateTime checkInDateTime);
    Long countByCheckInDateTimeIsNotNullAndCheckOutDateTimeIsNull();

    @Query("SELECT COUNT(c) FROM Checkin c WHERE c.checkInDateTime BETWEEN :startOfWeek AND :endOfWeek")
    Long countByCheckinDateBetweenThisWeek(@Param("startOfWeek") LocalDateTime startOfWeek, @Param("endOfWeek") LocalDateTime endOfWeek);

    @Query("SELECT COUNT(c) FROM Checkin c WHERE c.checkInDateTime BETWEEN :startOfMonth AND :endOfMonth")
    Long countByCheckinDateBetweenThisMonth(@Param("startOfMonth") LocalDateTime startOfMonth, @Param("endOfMonth") LocalDateTime endOfMonth);

    @Query("SELECT COUNT(c) FROM Checkin c WHERE c.checkInDateTime BETWEEN :startOfYear AND :endOfYear")
    Long countByCheckinDateBetweenThisYear();

}

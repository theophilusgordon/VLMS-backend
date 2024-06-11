package com.theophilusgordon.guestlogixserver.visit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface VisitRepository extends JpaRepository<Visit, Integer> {
    Iterable<Visit> findByGuestId(String guestId);
    Iterable<Visit> findByHostId(String hostId);
    
    Iterable<Visit> findByCheckInDateTime(LocalDateTime checkIn);

    Iterable<Visit> findByCheckInDateTimeBetween(LocalDateTime start, LocalDateTime end);

    Iterable<Visit> findByHostIdAndCheckInDateTimeBetween(String hostId, LocalDateTime start, LocalDateTime end);

    Long countByCheckInDateTimeEquals(LocalDateTime checkInDateTime);
    Long countByCheckInDateTimeIsNotNullAndCheckOutDateTimeIsNull();

    @Query("SELECT c FROM Visit c WHERE c.checkOutDateTime IS NULL")
    Iterable<Visit> findCurrentGuests();

    @Query("SELECT COUNT(c) FROM Visit c WHERE c.checkInDateTime BETWEEN :startOfWeek AND :endOfWeek")
    Long countByCheckinDateBetweenThisWeek(@Param("startOfWeek") LocalDateTime startOfWeek, @Param("endOfWeek") LocalDateTime endOfWeek);

    @Query("SELECT COUNT(c) FROM Visit c WHERE c.checkInDateTime BETWEEN :startOfMonth AND :endOfMonth")
    Long countByCheckinDateBetweenThisMonth(@Param("startOfMonth") LocalDateTime startOfMonth, @Param("endOfMonth") LocalDateTime endOfMonth);

    @Query("SELECT COUNT(c) FROM Visit c WHERE c.checkInDateTime BETWEEN :startOfYear AND :endOfYear")
    Long countByCheckinDateBetweenThisYear();

}

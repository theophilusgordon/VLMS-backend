package com.theophilusgordon.vlmsbackend.visit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface VisitRepository extends JpaRepository<Visit, Integer> {
    List<Visit> findByGuestId(UUID guestId);
    List<Visit> findByHostId(UUID hostId);
    
    List<Visit> findByCheckInDateTime(LocalDateTime checkIn);

    List<Visit> findByCheckInDateTimeBetween(LocalDateTime start, LocalDateTime end);

    List<Visit> findByHostIdAndCheckInDateTimeBetween(UUID hostId, LocalDateTime start, LocalDateTime end);

    Long countByCheckInDateTimeEquals(LocalDateTime checkInDateTime);
    Long countByCheckInDateTimeIsNotNullAndCheckOutDateTimeIsNull();

    @Query("SELECT c FROM Visit c WHERE c.checkOutDateTime IS NULL")
    List<Visit> findCurrentGuests();

    @Query("SELECT COUNT(c) FROM Visit c WHERE c.checkInDateTime BETWEEN :startOfWeek AND :endOfWeek")
    Long countByCheckinDateBetweenThisWeek(@Param("startOfWeek") LocalDateTime startOfWeek, @Param("endOfWeek") LocalDateTime endOfWeek);

    @Query("SELECT COUNT(c) FROM Visit c WHERE c.checkInDateTime BETWEEN :startOfMonth AND :endOfMonth")
    Long countByCheckinDateBetweenThisMonth(@Param("startOfMonth") LocalDateTime startOfMonth, @Param("endOfMonth") LocalDateTime endOfMonth);

    @Query("SELECT COUNT(c) FROM Visit c WHERE c.checkInDateTime BETWEEN :startOfYear AND :endOfYear")
    Long countByCheckinDateBetweenThisYear();

}

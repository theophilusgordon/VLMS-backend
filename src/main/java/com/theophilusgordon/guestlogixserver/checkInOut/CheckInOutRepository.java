package com.theophilusgordon.guestlogixserver.checkInOut;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface CheckInOutRepository extends JpaRepository<CheckInOut, Integer> {
    Iterable<CheckInOut> findByGuestId(String guestId);
    Iterable<CheckInOut> findByHostId(String hostId);
    
    Iterable<CheckInOut> findByCheckIn(LocalDateTime checkIn);

    Iterable<CheckInOut> findByCheckInBetween(LocalDateTime start, LocalDateTime end);

    Iterable<CheckInOut> findByHostIdAndCheckInBetween(String hostId, LocalDateTime start, LocalDateTime end);

}

package com.theophilusgordon.guestlogixserver.checkIn;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface CheckInRepository extends JpaRepository<CheckIn, Integer> {
    Iterable<CheckIn> findByGuestId(String guestId);
    Iterable<CheckIn> findByHostId(String hostId);
    
    Iterable<CheckIn> findByCheckInDateTime(LocalDateTime checkIn);

    Iterable<CheckIn> findByCheckInDateTimeBetween(LocalDateTime start, LocalDateTime end);

    Iterable<CheckIn> findByHostIdAndCheckInDateTimeBetween(String hostId, LocalDateTime start, LocalDateTime end);

}

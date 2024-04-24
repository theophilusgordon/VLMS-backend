package com.theophilusgordon.guestlogixserver.checkin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface CheckinRepository extends JpaRepository<Checkin, Integer> {
    Iterable<Checkin> findByGuestId(String guestId);
    Iterable<Checkin> findByHostId(String hostId);
    
    Iterable<Checkin> findByCheckInDateTime(LocalDateTime checkIn);

    Iterable<Checkin> findByCheckInDateTimeBetween(LocalDateTime start, LocalDateTime end);

    Iterable<Checkin> findByHostIdAndCheckInDateTimeBetween(String hostId, LocalDateTime start, LocalDateTime end);

}

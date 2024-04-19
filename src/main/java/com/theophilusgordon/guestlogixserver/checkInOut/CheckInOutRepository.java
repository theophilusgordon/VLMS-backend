package com.theophilusgordon.guestlogixserver.checkInOut;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckInOutRepository extends JpaRepository<CheckInOut, Integer> {
}

package com.theophilusgordon.guestlogixserver.clocking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClockingRepository extends JpaRepository<Clocking, Integer> {
}

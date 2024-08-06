package com.theophilusgordon.vlmsbackend.clocking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface ClockingRepository extends JpaRepository<Clocking, Integer> {
    Page<Clocking> findAllByUserId(UUID userId, Pageable pageable);
}

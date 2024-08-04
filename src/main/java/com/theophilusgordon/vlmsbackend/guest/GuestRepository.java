package com.theophilusgordon.vlmsbackend.guest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GuestRepository extends JpaRepository<Guest, UUID>{

    Optional<Guest> findByEmail(String email);
    Boolean existsByEmail(String email);
    Page<Guest> findAllByFullNameContaining(String query, Pageable pageable);
    long count();
}


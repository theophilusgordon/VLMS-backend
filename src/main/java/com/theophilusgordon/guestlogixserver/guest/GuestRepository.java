package com.theophilusgordon.guestlogixserver.guest;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<Guest, String>{
    Boolean existsByEmail(String email);
}

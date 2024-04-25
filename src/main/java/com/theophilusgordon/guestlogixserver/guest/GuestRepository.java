package com.theophilusgordon.guestlogixserver.guest;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest, String>{
    Boolean existsByEmail(String email);
    Optional<Guest> findByQrCode(byte[] qrCode);
}

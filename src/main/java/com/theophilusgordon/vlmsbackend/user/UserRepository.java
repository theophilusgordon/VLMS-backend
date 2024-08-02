package com.theophilusgordon.vlmsbackend.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    Page<User> findAllByRole(Role role, Pageable pageable);
    Page<User> findAllByRoleAndFullNameContaining(Role role, String fullName, Pageable pageable);
    Integer countByRole(Role role);

}

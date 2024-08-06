package com.theophilusgordon.vlmsbackend.clocking;

import com.theophilusgordon.vlmsbackend.exception.NotFoundException;
import com.theophilusgordon.vlmsbackend.user.User;
import com.theophilusgordon.vlmsbackend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClockingService {
    public final ClockingRepository clockingRepository;
    private final UserRepository userRepository;

    public ClockingResponse clockIn(ClockInRequest clockInRequest) {
        Clocking clocking = new Clocking();
        clocking.setUserId(clockInRequest.userId());
        clocking.setWorkLocation(createWorkLocation(clockInRequest.workLocation()));
        clocking.setClockInDateTime(LocalDateTime.now());
        clockingRepository.save(clocking);
        User user = userRepository.findById(UUID.fromString(clockInRequest.userId()))
                .orElseThrow(() -> new NotFoundException("User", clockInRequest.userId()));

        return this.buildClockingResponse(clocking, user);
    }

    public ClockingResponse clockOut(ClockOutRequest clockOutRequest) {
        Clocking clocking = clockingRepository.findById(clockOutRequest.clockingId()).orElseThrow(() -> new NotFoundException("Clocking", String.valueOf(clockOutRequest.clockingId())));
        System.out.println("*********clocking: " + clocking);

        clocking.setClockOutDateTime(LocalDateTime.now());
        clockingRepository.save(clocking);
        User user = userRepository.findById(UUID.fromString(clocking.getUserId()))
                .orElseThrow(() -> new NotFoundException("User", clocking.getUserId()));

        return this.buildClockingResponse(clocking, user);
    }

    private WorkLocation createWorkLocation(String value) {
        for (WorkLocation workLocation : WorkLocation.values()) {
            if (workLocation.name().equalsIgnoreCase(value)) {
                return workLocation;
            }
        }
        throw new IllegalArgumentException("Invalid role: " + value);
    }

    private ClockingResponse buildClockingResponse(Clocking clocking, User user) {
        return ClockingResponse.builder()
                .id(clocking.getId())
                .user(User.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .firstName(user.getFullName())
                        .middleName(user.getMiddleName())
                        .lastName(user.getLastName())
                        .phone(user.getPhone())
                        .profilePhotoUrl(user.getProfilePhotoUrl())
                        .department(user.getDepartment())
                        .role(user.getRole())
                        .build())
                .workLocation(String.valueOf(clocking.getWorkLocation()))
                .clockInDateTime(String.valueOf(clocking.getClockInDateTime()))
                .clockOutDateTime(String.valueOf(clocking.getClockOutDateTime()))
                .build();
    }
}

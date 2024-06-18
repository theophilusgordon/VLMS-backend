package com.theophilusgordon.guestlogixserver.clocking;

import com.theophilusgordon.guestlogixserver.exception.NotFoundException;
import com.theophilusgordon.guestlogixserver.user.User;
import com.theophilusgordon.guestlogixserver.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ClockingService {
    public final ClockingRepository clockingRepository;
    private final UserRepository userRepository;

    public ClockingResponse clockIn(ClockInRequest clockInRequest) {
        Clocking clocking = new Clocking();
        clocking.setUserId(clockInRequest.getUserId());
        clocking.setWorkLocation(createWorkLocation(clockInRequest.getWorkLocation()));
        clocking.setClockInDateTime(LocalDateTime.now());
        clockingRepository.save(clocking);
        User user = userRepository.findById(clockInRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("User", clockInRequest.getUserId()));

        return this.buildClockingResponse(clocking, user);
    }

    public ClockingResponse clockOut(ClockOutRequest clockOutRequest) {
        Clocking clocking = clockingRepository.findById(clockOutRequest.getClockingId()).orElseThrow(() -> new NotFoundException("Clocking", String.valueOf(clockOutRequest.getClockingId())));
        clocking.setClockOutDateTime(LocalDateTime.now());
        clockingRepository.save(clocking);
        User user = userRepository.findById(clocking.getUserId())
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

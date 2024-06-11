package com.theophilusgordon.guestlogixserver.clocking;

import com.theophilusgordon.guestlogixserver.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ClockingService {
    public final ClockingRepository clockingRepository;

    public ClockingResponse clockIn(ClockInRequest clockInRequest) {
        Clocking clocking = new Clocking();
        clocking.setUserId(clockInRequest.getUserId());
        clocking.setWorkLocation(WorkLocation.OFFICE);
        clocking.setClockInDateTime(LocalDateTime.now());
        clockingRepository.save(clocking);
        return ClockingResponse.builder()
                .id(clocking.getId())
                .userId(clocking.getUserId())
                .clockInDateTime(String.valueOf(clocking.getClockInDateTime()))
                .build();
    }

    public ClockingResponse clockOut(ClockOutRequest clockOutRequest) {
        Clocking clocking = clockingRepository.findById(clockOutRequest.getClockingId()).orElseThrow(() -> new NotFoundException("Clocking", String.valueOf(clockOutRequest.getClockingId())));
        clocking.setClockOutDateTime(LocalDateTime.now());
        clockingRepository.save(clocking);
        return ClockingResponse.builder()
                .id(clocking.getId())
                .userId(clocking.getUserId())
                .clockInDateTime(String.valueOf(clocking.getClockInDateTime()))
                .clockOutDateTime(String.valueOf(clocking.getClockOutDateTime()))
                .build();
    }
}

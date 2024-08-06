package com.theophilusgordon.vlmsbackend.clocking;

import com.theophilusgordon.vlmsbackend.exception.NotFoundException;
import com.theophilusgordon.vlmsbackend.user.User;
import com.theophilusgordon.vlmsbackend.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClockingServiceTest {

    @Mock
    private ClockingRepository clockingRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ClockingService clockingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    UUID uuid = UUID.randomUUID();

//    @Test
//    void clockIn_ValidRequest_Success() {
//        ClockInRequest request = new ClockInRequest(String.valueOf(uuid), "remote");
//        User user = new User();
//        user.setId(UUID.fromString(String.valueOf(uuid)));
//        Clocking clocking = new Clocking();
//        clocking.setUserId(String.valueOf(uuid));
//        clocking.setClockInDateTime(LocalDateTime.now());
//
//        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
//        when(clockingRepository.save(any(Clocking.class))).thenReturn(clocking);
//
//        ClockingResponse response = clockingService.clockIn(request);
//
//        assertNotNull(response);
//        assertEquals(String.valueOf(uuid), response.getUser().getId().toString());
//        verify(clockingRepository, times(1)).save(any(Clocking.class));
//    }
//
//    @Test
//    void clockIn_UserNotFound_ThrowsNotFoundException() {
//        ClockInRequest request = new ClockInRequest(String.valueOf(uuid), "remote");
//
//        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
//
//        assertThrows(NotFoundException.class, () -> clockingService.clockIn(request));
//    }

    @Test
    void clockOut_ValidRequest_Success() {
        ClockOutRequest request = new ClockOutRequest(12);
        User user = new User();
        user.setId(UUID.fromString(String.valueOf(uuid)));
        Clocking clocking = new Clocking();
        clocking.setUserId(uuid);
        clocking.setClockOutDateTime(LocalDateTime.now());

        when(clockingRepository.findById(any(Integer.class))).thenReturn(Optional.of(clocking));
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(clockingRepository.save(any(Clocking.class))).thenReturn(clocking);

        ClockingResponse response = clockingService.clockOut(request);

        assertNotNull(response);
        assertEquals(String.valueOf(uuid), response.getUser().getId().toString());
        verify(clockingRepository, times(1)).save(any(Clocking.class));
    }

    @Test
    void clockOut_ClockingNotFound_ThrowsNotFoundException() {
        ClockOutRequest request = new ClockOutRequest(12);

        when(clockingRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> clockingService.clockOut(request));
    }
}
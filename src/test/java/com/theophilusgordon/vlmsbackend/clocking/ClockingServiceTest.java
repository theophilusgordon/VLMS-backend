package com.theophilusgordon.vlmsbackend.clocking;

import com.theophilusgordon.vlmsbackend.user.User;
import com.theophilusgordon.vlmsbackend.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ClockingServiceTest {
    @Mock
    private ClockingRepository clockingRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ClockingService clockingService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testClockIn() {
        ClockInRequest request = new ClockInRequest(
                "123",
                "REMOTE"
        );
        request.setUserId("123");
        request.setWorkLocation("REMOTE");

        Clocking clocking = new Clocking();
        clocking.setUserId(request.getUserId());
        clocking.setWorkLocation(WorkLocation.REMOTE);
        clocking.setClockInDateTime(LocalDateTime.now());

        User user = new User();
        user.setId("123");

        when(clockingRepository.save(any(Clocking.class))).thenReturn(clocking);
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        ClockingResponse response = clockingService.clockIn(request);

        assertEquals(clocking.getUserId(), response.getUser().getId());
        verify(clockingRepository, times(1)).save(any(Clocking.class));
        verify(userRepository, times(1)).findById(anyString());
    }

    @Test
    void testClockOut() {
        ClockOutRequest request = new ClockOutRequest(1);
        request.setClockingId(1);

        Clocking clocking = new Clocking();
        clocking.setId(request.getClockingId());
        clocking.setUserId("123");
        clocking.setWorkLocation(WorkLocation.REMOTE);
        clocking.setClockInDateTime(LocalDateTime.now());

        User user = new User();
        user.setId("123");

        when(clockingRepository.findById(request.getClockingId())).thenReturn(Optional.of(clocking));
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        ClockingResponse response = clockingService.clockOut(request);

        assertEquals(clocking.getId(), response.getId());
        verify(clockingRepository, times(1)).findById(request.getClockingId());
        verify(clockingRepository, times(1)).save(clocking);
        verify(userRepository, times(1)).findById(anyString());
    }
}
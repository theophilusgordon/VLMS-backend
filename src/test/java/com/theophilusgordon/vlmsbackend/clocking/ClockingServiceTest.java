package com.theophilusgordon.vlmsbackend.clocking;

import com.theophilusgordon.vlmsbackend.user.User;
import com.theophilusgordon.vlmsbackend.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.Collections;
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

    @Test
    void testClockIn_Success() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("user@example.com");

        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setEmail("user@example.com");

        ClockInRequest clockInRequest = new ClockInRequest("remote");

        when(userRepository.findByEmail(principal.getName())).thenReturn(Optional.of(user));
        when(clockingRepository.save(any(Clocking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ClockingResponse response = clockingService.clockIn(clockInRequest, principal);

        assertNotNull(response);
        assertEquals(userId, response.getUser().getId());

        ArgumentCaptor<Clocking> clockingCaptor = ArgumentCaptor.forClass(Clocking.class);
        verify(clockingRepository).save(clockingCaptor.capture());
        Clocking savedClocking = clockingCaptor.getValue();
        assertEquals(userId, savedClocking.getUserId());
        assertNotNull(savedClocking.getClockInDateTime());
    }

    @Test
    void testClockOut_Success() {
        UUID userId = UUID.randomUUID();
        int clockingId = 1;

        User user = new User();
        user.setId(userId);

        Clocking clocking = new Clocking();
        clocking.setUserId(userId);

        ClockOutRequest clockOutRequest = new ClockOutRequest(clockingId);

        when(clockingRepository.findById(clockingId)).thenReturn(Optional.of(clocking));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(clockingRepository.save(any(Clocking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ClockingResponse response = clockingService.clockOut(clockOutRequest);

        assertNotNull(response);
        assertEquals(userId, response.getUser().getId());
        assertNotNull(response.getClockOutDateTime());

        ArgumentCaptor<Clocking> clockingCaptor = ArgumentCaptor.forClass(Clocking.class);
        verify(clockingRepository).save(clockingCaptor.capture());
        Clocking savedClocking = clockingCaptor.getValue();
        assertNotNull(savedClocking.getClockOutDateTime());
    }

    @Test
    void testGetClockings_Success() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        Clocking clocking = new Clocking();
        clocking.setUserId(userId);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Clocking> clockingPage = new PageImpl<>(Collections.singletonList(clocking), pageable, 1);

        when(clockingRepository.findAll(pageable)).thenReturn(clockingPage);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Page<ClockingResponse> responsePage = clockingService.getClockings(pageable);

        assertNotNull(responsePage);
        assertEquals(1, responsePage.getTotalElements());
    }

    @Test
    void testGetClockingsByUserId_Success() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        Clocking clocking = new Clocking();
        clocking.setUserId(userId);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Clocking> clockingPage = new PageImpl<>(Collections.singletonList(clocking), pageable, 1);

        when(clockingRepository.findAllByUserId(userId, pageable)).thenReturn(clockingPage);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Page<ClockingResponse> responsePage = clockingService.getClockingsByUserId(userId, pageable);

        assertNotNull(responsePage);
        assertEquals(1, responsePage.getTotalElements());
    }

    @Test
    void testGetCurrentUserClocking_Success() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("user@example.com");

        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setEmail("user@example.com");

        Clocking clocking = new Clocking();
        clocking.setUserId(userId);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Clocking> clockingPage = new PageImpl<>(Collections.singletonList(clocking), pageable, 1);

        when(userRepository.findByEmail(principal.getName())).thenReturn(Optional.of(user));
        when(clockingRepository.findAllByUserId(userId, pageable)).thenReturn(clockingPage);

        Page<ClockingResponse> responsePage = clockingService.getCurrentUserClocking(principal, pageable);

        assertNotNull(responsePage);
        assertEquals(1, responsePage.getTotalElements());
    }
}

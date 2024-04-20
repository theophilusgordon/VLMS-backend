package com.theophilusgordon.guestlogixserver.checkIn;

import com.theophilusgordon.guestlogixserver.exception.NotFoundException;
import com.theophilusgordon.guestlogixserver.guest.Guest;
import com.theophilusgordon.guestlogixserver.guest.GuestRepository;
import com.theophilusgordon.guestlogixserver.user.User;
import com.theophilusgordon.guestlogixserver.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CheckInServiceTest {

    @InjectMocks
    private CheckInService checkInService;

    @Mock
    private CheckInRepository checkInRepository;

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheckIn() {
        CheckInRequest request = new CheckInRequest();
        request.setGuestId("guestId");
        request.setHostId("hostId");

        Guest guest = new Guest();
        User host = new User();

        when(guestRepository.findById(anyString())).thenReturn(Optional.of(guest));
        when(userRepository.findById(anyString())).thenReturn(Optional.of(host));

        checkInService.checkIn(request);

        verify(checkInRepository, times(1)).save(any(CheckIn.class));
    }

    @Test
    void testCheckOut() {
        when(checkInRepository.findById(anyInt())).thenReturn(Optional.of(new CheckIn()));

        checkInService.checkOut(1);

        verify(checkInRepository, times(1)).findById(anyInt());
    }

    @Test
    void testCheckOutNotFound() {
        when(checkInRepository.findById(anyInt())).thenReturn(Optional.empty());

        try {
            checkInService.checkOut(1);
        } catch (NotFoundException e) {
            // Expected exception
        }

        verify(checkInRepository, times(1)).findById(anyInt());
    }
}
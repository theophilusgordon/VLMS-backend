package com.theophilusgordon.guestlogixserver.checkInOut;

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

class CheckInOutServiceTest {

    @InjectMocks
    private CheckInOutService checkInOutService;

    @Mock
    private CheckInOutRepository checkInOutRepository;

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

        checkInOutService.checkIn(request);

        verify(checkInOutRepository, times(1)).save(any(CheckInOut.class));
    }

    @Test
    void testCheckOut() {
        when(checkInOutRepository.findById(anyInt())).thenReturn(Optional.of(new CheckInOut()));

        checkInOutService.checkOut(1);

        verify(checkInOutRepository, times(1)).findById(anyInt());
    }

    @Test
    void testCheckOutNotFound() {
        when(checkInOutRepository.findById(anyInt())).thenReturn(Optional.empty());

        try {
            checkInOutService.checkOut(1);
        } catch (NotFoundException e) {
            // Expected exception
        }

        verify(checkInOutRepository, times(1)).findById(anyInt());
    }
}
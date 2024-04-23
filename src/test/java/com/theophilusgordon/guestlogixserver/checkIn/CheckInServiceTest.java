package com.theophilusgordon.guestlogixserver.checkIn;

import com.theophilusgordon.guestlogixserver.exception.BadRequestException;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private UserRepository hostRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCheckIns() {
        when(checkInRepository.findAll()).thenReturn(new ArrayList<>());

        checkInService.getCheckIns();

        verify(checkInRepository, times(1)).findAll();
    }

    @Test
    void testCheckIn() {
        CheckInRequest request = new CheckInRequest();
        request.setGuestId("guestId");
        request.setHostId("hostId");

        Guest guest = new Guest();
        User host = new User();

        when(guestRepository.findById(anyString())).thenReturn(Optional.of(guest));
        when(hostRepository.findById(anyString())).thenReturn(Optional.of(host));

        checkInService.checkIn(request);

        verify(checkInRepository, times(1)).save(any(CheckIn.class));
    }

    @Test
    void testGetCheckInNotFound() {
        when(checkInRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> checkInService.getCheckIn(1));
    }

    @Test
    void testGetCheckInsByGuest() {
        when(guestRepository.existsById(anyString())).thenReturn(true);
        when(checkInRepository.findByGuestId(anyString())).thenReturn(new ArrayList<>());

        checkInService.getCheckInsByGuest("guestId");

        verify(checkInRepository, times(1)).findByGuestId(anyString());
    }

    @Test
    void testGetCheckInsByGuestNotFound() {
        when(guestRepository.existsById(anyString())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> checkInService.getCheckInsByGuest("guestId"));
    }

    @Test
    void testGetCheckInsByHost() {
        when(hostRepository.existsById(anyString())).thenReturn(true);
        when(checkInRepository.findByHostId(anyString())).thenReturn(new ArrayList<>());

        checkInService.getCheckInsByHost("hostId");

        verify(checkInRepository, times(1)).findByHostId(anyString());
    }

    @Test
    void testGetCheckInsByHostNotFound() {
        when(hostRepository.existsById(anyString())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> checkInService.getCheckInsByHost("hostId"));
    }

    @Test
    void testGetCheckInsByCheckInDate() {
        String checkInDate = LocalDateTime.now().toString();
        when(checkInRepository.findByCheckInDateTime(any(LocalDateTime.class))).thenReturn(new ArrayList<>());

        checkInService.getCheckInsByCheckInDate(checkInDate);

        verify(checkInRepository, times(1)).findByCheckInDateTime(any(LocalDateTime.class));
    }

    @Test
    void testGetCheckInsByCheckInDateInvalidDate() {
        String checkInDate = "invalidDate";

        assertThrows(BadRequestException.class, () -> checkInService.getCheckInsByCheckInDate(checkInDate));
    }

    @Test
    void testGetCheckInsByPeriod() {
        String start = LocalDateTime.now().toString();
        String end = LocalDateTime.now().plusDays(1).toString();
        when(checkInRepository.findByCheckInDateTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(new ArrayList<>());

        checkInService.getCheckInsByPeriod(start, end);

        verify(checkInRepository, times(1)).findByCheckInDateTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void testGetCheckInsByPeriodInvalidDates() {
        String start = "invalidStart";
        String end = "invalidEnd";

        assertThrows(BadRequestException.class, () -> checkInService.getCheckInsByPeriod(start, end));
    }

    @Test
    void testGetCheckInsByHostAndPeriod() {
        String hostId = "hostId";
        String start = LocalDateTime.now().toString();
        String end = LocalDateTime.now().plusDays(1).toString();

        when(hostRepository.existsById(anyString())).thenReturn(true);
        when(checkInRepository.findByHostIdAndCheckInDateTimeBetween(anyString(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(new ArrayList<>());

        checkInService.getCheckInsByHostAndPeriod(hostId, start, end);

        verify(checkInRepository, times(1)).findByHostIdAndCheckInDateTimeBetween(anyString(), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void testGetCheckInsByHostAndPeriodHostNotFound() {
        String hostId = "hostId";
        String start = LocalDateTime.now().toString();
        String end = LocalDateTime.now().plusDays(1).toString();

        when(hostRepository.existsById(anyString())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> checkInService.getCheckInsByHostAndPeriod(hostId, start, end));
    }

    @Test
    void testGetCheckInsByHostAndPeriodInvalidDates() {
        String hostId = "hostId";
        String start = "invalidStart";
        String end = "invalidEnd";

        when(hostRepository.existsById(anyString())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> checkInService.getCheckInsByHostAndPeriod(hostId, start, end));
    }
}
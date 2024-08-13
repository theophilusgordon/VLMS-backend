package com.theophilusgordon.vlmsbackend.visit;

import com.theophilusgordon.vlmsbackend.constants.ExceptionConstants;
import com.theophilusgordon.vlmsbackend.exception.BadRequestException;
import com.theophilusgordon.vlmsbackend.exception.NotFoundException;
import com.theophilusgordon.vlmsbackend.guest.Guest;
import com.theophilusgordon.vlmsbackend.guest.GuestRepository;
import com.theophilusgordon.vlmsbackend.user.User;
import com.theophilusgordon.vlmsbackend.user.UserRepository;
import com.theophilusgordon.vlmsbackend.utils.email.EmailService;
import com.theophilusgordon.vlmsbackend.utils.QRCodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitServiceTest {

    @Mock
    private VisitRepository visitRepository;

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private QRCodeService qrCodeService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private VisitService visitService;

    private Guest guest;
    private User host;
    private Visit visit;

    @BeforeEach
    void setUp() {
        guest = Guest.builder()
                .id(UUID.randomUUID())
                .email("guest@example.com")
                .firstName("GuestFirstName")
                .build();

        host = User.builder()
                .id(UUID.randomUUID())
                .email("host@example.com")
                .firstName("HostFirstName")
                .build();

        visit = Visit.builder()
                .id(1)
                .guest(guest)
                .host(host)
                .checkInDateTime(LocalDateTime.now())
                .build();
    }

    @Test
    void testCheckIn_Success() {
        VisitRequest request = new VisitRequest(guest.getId().toString(), host.getId().toString());

        when(guestRepository.findById(any(UUID.class))).thenReturn(Optional.of(guest));
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(host));
        when(visitRepository.existsByGuestIdAndCheckOutDateTimeIsNull(any(UUID.class))).thenReturn(false);
        when(qrCodeService.generateQRCodeImage(anyString())).thenReturn("QRCodeImage".getBytes());
        when(visitRepository.save(any(Visit.class))).thenReturn(visit);

        VisitResponse response = visitService.checkIn(request);

        assertNotNull(response);
        verify(emailService, times(1))
                .sendCheckinSuccessEmail(eq(guest.getEmail()), eq(guest.getFirstName()), any(byte[].class));
        verify(emailService, times(1))
                .sendCheckinNotificationEmail(eq(host.getEmail()), eq(host.getFirstName()), eq(guest), any(LocalDateTime.class));
    }

    @Test
    void testCheckIn_GuestAlreadyCheckedIn() {
        VisitRequest request = new VisitRequest(guest.getId().toString(), host.getId().toString());

        when(guestRepository.findById(any(UUID.class))).thenReturn(Optional.of(guest));
        when(visitRepository.existsByGuestIdAndCheckOutDateTimeIsNull(any(UUID.class))).thenReturn(true);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> visitService.checkIn(request));
        assertEquals(ExceptionConstants.GUEST_ALREADY_CHECKED_IN, exception.getMessage());
    }

    @Test
    void testCheckOut_Success() {
        when(visitRepository.findById(anyInt())).thenReturn(Optional.of(visit));
        when(visitRepository.save(any(Visit.class))).thenReturn(visit);

        VisitResponse response = visitService.checkOut(visit.getId());

        assertNotNull(response);
        verify(emailService, times(1)).sendCheckoutSuccessEmail(eq(guest.getEmail()), eq(guest.getFirstName()), any(LocalDateTime.class));
        verify(emailService, times(1)).sendCheckoutNotificationEmail(eq(host.getEmail()), eq(host.getFirstName()), eq(guest), any(LocalDateTime.class));
    }

    @Test
    void testCheckOut_GuestAlreadyCheckedOut() {
        visit.setCheckOutDateTime(LocalDateTime.now());

        when(visitRepository.findById(anyInt())).thenReturn(Optional.of(visit));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> visitService.checkOut(visit.getId()));
        assertEquals(ExceptionConstants.GUEST_ALREADY_CHECKED_OUT, exception.getMessage());
    }

    @Test
    void testGetCheckIn_Success() {
        when(visitRepository.findById(anyInt())).thenReturn(Optional.of(visit));

        VisitResponse response = visitService.getCheckIn(visit.getId());

        assertNotNull(response);
        assertEquals(visit.getId(), response.getId());
    }

    @Test
    void testGetCheckIn_NotFound() {
        when(visitRepository.findById(anyInt())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> visitService.getCheckIn(visit.getId()));
        assertEquals(exception.getMessage(), exception.getMessage());
    }

    @Test
    void testGetCheckIns_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Visit> visitPage = new PageImpl<>(Collections.singletonList(visit));

        when(visitRepository.findAll(any(Pageable.class))).thenReturn(visitPage);

        Page<VisitResponse> result = visitService.getCheckIns(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testGetCheckInsByGuest_Success() {
        when(guestRepository.existsById(any(UUID.class))).thenReturn(true);
        when(visitRepository.findByGuestId(any(UUID.class))).thenReturn(Collections.singletonList(visit));

        List<VisitResponse> result = visitService.getCheckInsByGuest(guest.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetCheckInsByGuest_NotFound() {
        when(guestRepository.existsById(any(UUID.class))).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> visitService.getCheckInsByGuest(guest.getId()));
        assertEquals(exception.getMessage(), exception.getMessage());
    }

    @Test
    void testGetCheckInsByHost_Success() {
        when(userRepository.existsById(any(UUID.class))).thenReturn(true);
        when(visitRepository.findByHostId(any(UUID.class))).thenReturn(Collections.singletonList(visit));

        List<VisitResponse> result = visitService.getCheckInsByHost(host.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetCheckInsByHost_NotFound() {
        when(userRepository.existsById(any(UUID.class))).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> visitService.getCheckInsByHost(host.getId()));
        assertEquals(exception.getMessage(), exception.getMessage());
    }

    @Test
    void testGetCheckInsByCheckInDate_Success() {
        LocalDateTime dateTime = LocalDateTime.now();
        when(visitRepository.findByCheckInDateTime(any(LocalDateTime.class))).thenReturn(Collections.singletonList(visit));

        List<VisitResponse> result = visitService.getCheckInsByCheckInDate(dateTime);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetCheckInsByCheckInDate_InvalidFormat() {
        BadRequestException exception = assertThrows(BadRequestException.class, () -> visitService.getCheckInsByCheckInDate(LocalDateTime.parse("invalid-date")));
        assertEquals("Invalid date format. Please use the ISO 8601 date time format: yyyy-MM-dd'T'HH:mm:ss", exception.getMessage());
    }

    @Test
    void testGetCheckInsByPeriod_Success() {
        LocalDateTime start = LocalDateTime.parse("2023-01-01T00:00:00");
        LocalDateTime end = LocalDateTime.parse("2023-12-31T23:59:59");
        when(visitRepository.findByCheckInDateTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(Collections.singletonList(visit));

        List<VisitResponse> result = visitService.getCheckInsByPeriod(start, end);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

        @Test
        void testGetTotalVisits() {
            List<Visit> visits = List.of(visit);

            when(visitRepository.findAll()).thenReturn(visits);

            Integer totalVisits = visitService.getTotalVisits();

            assertNotNull(totalVisits);
            assertEquals(visits.size(), totalVisits);
        }

        @Test
        void testGetTotalCurrentGuests() {
            Long count = 5L;

            when(visitRepository.countByCheckInDateTimeIsNotNullAndCheckOutDateTimeIsNull()).thenReturn(count);

            Integer totalCurrentGuests = visitService.getTotalCurrentGuests();

            assertNotNull(totalCurrentGuests);
            assertEquals(count.intValue(), totalCurrentGuests);
        }

        @Test
        void testGetTotalVisitsBetween_Success() {
            LocalDateTime start = LocalDateTime.parse("2023-01-01T00:00:00");
            LocalDateTime end = LocalDateTime.parse("2023-12-31T23:59:59");
            List<Visit> visits = List.of(visit);

            when(visitRepository.findByCheckInDateTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                    .thenReturn(visits);

            Integer totalVisitsBetween = visitService.getTotalVisitsBetween(start, end);

            assertNotNull(totalVisitsBetween);
            assertEquals(visits.size(), totalVisitsBetween.intValue());
        }

        @Test
        void testGetTotalVisitsBetween_InvalidFormat() {
            LocalDateTime start = LocalDateTime.parse("invalid-date");
            LocalDateTime end = LocalDateTime.parse("2023-12-31T23:59:59");

            BadRequestException exception = assertThrows(BadRequestException.class, () -> visitService.getTotalVisitsBetween(start, end));
            assertEquals("Invalid date format. Please use the ISO 8601 date time format: yyyy-MM-dd'T'HH:mm:ss", exception.getMessage());
        }

}

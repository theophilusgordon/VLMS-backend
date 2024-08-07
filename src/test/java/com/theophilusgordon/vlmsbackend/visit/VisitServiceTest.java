package com.theophilusgordon.vlmsbackend.visit;

import com.theophilusgordon.vlmsbackend.constants.ExceptionConstants;
import com.theophilusgordon.vlmsbackend.exception.BadRequestException;
import com.theophilusgordon.vlmsbackend.guest.Guest;
import com.theophilusgordon.vlmsbackend.guest.GuestRepository;
import com.theophilusgordon.vlmsbackend.user.User;
import com.theophilusgordon.vlmsbackend.user.UserRepository;
import com.theophilusgordon.vlmsbackend.utils.email.EmailService;
import com.theophilusgordon.vlmsbackend.utils.QRCodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Captor
    private ArgumentCaptor<String> emailCaptor;
    @Captor
    private ArgumentCaptor<String> nameCaptor;
    @Captor
    private ArgumentCaptor<LocalDateTime> dateTimeCaptor;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheckIn_Success() {
        String guestId = UUID.randomUUID().toString();
        String hostId = UUID.randomUUID().toString();
        VisitRequest request = new VisitRequest(guestId, hostId);

        Guest guest = new Guest();
        guest.setId(UUID.fromString(guestId));
        guest.setEmail("guest@example.com");
        guest.setFirstName("Guest");

        User host = new User();
        host.setId(UUID.fromString(hostId));
        host.setEmail("host@example.com");
        host.setFirstName("Host");

        when(guestRepository.findById(UUID.fromString(guestId))).thenReturn(Optional.of(guest));
        when(userRepository.findById(UUID.fromString(hostId))).thenReturn(Optional.of(host));
        when(qrCodeService.generateQRCodeImage(anyString())).thenReturn(new byte[0]);

        VisitResponse response = visitService.checkIn(request);

        assertNotNull(response);
        verify(visitRepository).save(any(Visit.class));
        verify(emailService).sendCheckinSuccessEmail(eq(guest.getEmail()), eq(guest.getFirstName()), any(byte[].class));
        verify(emailService).sendCheckinNotificationEmail(eq(host.getEmail()), eq(host.getFirstName()), eq(guest), any(LocalDateTime.class));
    }

    @Test
    void testCheckIn_GuestAlreadyCheckedIn() {
        String guestId = UUID.randomUUID().toString();
        String hostId = UUID.randomUUID().toString();
        VisitRequest request = new VisitRequest(guestId, hostId);

        Guest guest = new Guest();
        guest.setId(UUID.fromString(guestId));

        when(guestRepository.findById(UUID.fromString(guestId))).thenReturn(Optional.of(guest));
        when(visitRepository.existsByGuestIdAndCheckOutDateTimeIsNull(UUID.fromString(guestId))).thenReturn(true);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> visitService.checkIn(request));
        assertEquals(ExceptionConstants.GUEST_ALREADY_CHECKED_IN, exception.getMessage());
    }

    @Test
    void testCheckOut_Success() {
        Integer visitId = 1;
        Guest guest = new Guest();
        guest.setEmail("guest@example.com");
        guest.setFirstName("Guest");

        User host = new User();
        host.setEmail("host@example.com");
        host.setFirstName("Host");

        Visit visit = new Visit();
        visit.setId(visitId);
        visit.setGuest(guest);
        visit.setHost(host);

        when(visitRepository.findById(visitId)).thenReturn(Optional.of(visit));

        VisitResponse response = visitService.checkOut(visitId);

        assertNotNull(response);
        verify(visitRepository).save(visit);

        verify(emailService).sendCheckoutSuccessEmail(emailCaptor.capture(), nameCaptor.capture(), dateTimeCaptor.capture());
        assertEquals("guest@example.com", emailCaptor.getValue());
        assertEquals("Guest", nameCaptor.getValue());

        verify(emailService).sendCheckoutNotificationEmail(emailCaptor.capture(), nameCaptor.capture(), any(Guest.class), dateTimeCaptor.capture());
        assertEquals("host@example.com", emailCaptor.getValue());
        assertEquals("Host", nameCaptor.getValue());

    }
        @Test
        void testCheckOut_GuestAlreadyCheckedOut() {
        Integer visitId = 1;
        Visit visit = new Visit();
        visit.setId(visitId);
        visit.setCheckOutDateTime(LocalDateTime.now());

        when(visitRepository.findById(visitId)).thenReturn(Optional.of(visit));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> visitService.checkOut(visitId));
        assertEquals(ExceptionConstants.GUEST_ALREADY_CHECKED_OUT, exception.getMessage());
    }
}
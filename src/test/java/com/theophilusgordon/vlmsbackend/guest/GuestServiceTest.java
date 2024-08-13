//package com.theophilusgordon.vlmsbackend.guest;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.theophilusgordon.vlmsbackend.exception.BadRequestException;
//import com.theophilusgordon.vlmsbackend.exception.NotFoundException;
//import com.theophilusgordon.vlmsbackend.utils.QRCodeService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import java.util.Collections;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class GuestServiceTest {
//
//    @Mock
//    private GuestRepository guestRepository;
//
//    @Mock
//    private ObjectMapper objectMapper;
//
//    @Mock
//    private QRCodeService qrCodeService;
//
//    @InjectMocks
//    private GuestService guestService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testRegisterGuest_Success() {
//        GuestRegisterRequest request = new GuestRegisterRequest(
//                "John",
//                "",
//                "Doe",
//                "test@example.com",
//                "0555555555555",
//                "url",
//                "VLMS");
//        Guest guest = new Guest();
//        guest.setEmail("test@example.com");
//        guest.setFirstName("John");
//        guest.setLastName("Doe");
//
//        when(guestRepository.findByEmail(request.email())).thenReturn(Optional.empty());
//        when(objectMapper.convertValue(request, Guest.class)).thenReturn(guest);
//        when(guestRepository.save(any(Guest.class))).thenReturn(guest);
//
//        Guest registeredGuest = guestService.registerGuest(request);
//
//        assertNotNull(registeredGuest);
//        assertEquals(request.email(), registeredGuest.getEmail());
//        verify(guestRepository).save(any(Guest.class));
//    }
//
//    @Test
//    void testRegisterGuest_EmailAlreadyExists() {
//        GuestRegisterRequest request = new GuestRegisterRequest(
//                "John",
//                "",
//                "Doe",
//                "test@example.com",
//                "0555555555555",
//                "url",
//                "VLMS");
//        Guest existingGuest = new Guest();
//        existingGuest.setEmail("test@example.com");
//
//        when(guestRepository.findByEmail(request.email())).thenReturn(Optional.of(existingGuest));
//
//        assertThrows(BadRequestException.class, () -> guestService.registerGuest(request));
//    }
//
//    @Test
//    void testGetGuests_Success() {
//        Guest guest = new Guest();
//        Pageable pageable = PageRequest.of(0, 10);
//        Page<Guest> guestPage = new PageImpl<>(Collections.singletonList(guest), pageable, 1);
//
//        when(guestRepository.findAll(pageable)).thenReturn(guestPage);
//
//        Page<Guest> responsePage = guestService.getGuests(pageable);
//
//        assertNotNull(responsePage);
//        assertEquals(1, responsePage.getTotalElements());
//    }
//
//    @Test
//    void testSearchGuests_Success() {
//        Guest guest = new Guest();
//        Pageable pageable = PageRequest.of(0, 10);
//        String query = "John";
//        Page<Guest> guestPage = new PageImpl<>(Collections.singletonList(guest), pageable, 1);
//
//        when(guestRepository.findAllByFullNameContaining(query, pageable)).thenReturn(guestPage);
//
//        Page<Guest> responsePage = guestService.searchGuests(pageable, query);
//
//        assertNotNull(responsePage);
//        assertEquals(1, responsePage.getTotalElements());
//    }
//
//    @Test
//    void testGetGuest_Success() {
//        UUID guestId = UUID.randomUUID();
//        Guest guest = new Guest();
//        guest.setId(guestId);
//
//        when(guestRepository.findById(guestId)).thenReturn(Optional.of(guest));
//
//        Guest foundGuest = guestService.getGuest(guestId.toString());
//
//        assertNotNull(foundGuest);
//        assertEquals(guestId, foundGuest.getId());
//    }
//
//    @Test
//    void testGetGuest_NotFound() {
//        UUID guestId = UUID.randomUUID();
//
//        when(guestRepository.findById(guestId)).thenReturn(Optional.empty());
//
//        assertThrows(NotFoundException.class, () -> guestService.getGuest(guestId.toString()));
//    }
//
//    @Test
//    void testGetTotalGuests_Success() {
//        long totalGuests = 5L;
//        when(guestRepository.count()).thenReturn(totalGuests);
//
//        Long count = guestService.getTotalGuests();
//
//        assertNotNull(count);
//        assertEquals(totalGuests, count);
//    }
//}

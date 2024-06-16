//package com.theophilusgordon.guestlogixserver.guest;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.ResponseEntity;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//class GuestControllerTest {
//
//    @Mock
//    private GuestService guestService;
//
//    @InjectMocks
//    private GuestController guestController;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void whenRegisterGuest_thenGuestIsCreated() {
//        GuestRegisterRequest request = new GuestRegisterRequest(
//                "John",
//                "Doe",
//                "Smith",
//                "john@example.com",
//                "1234567890",
//                "http://example.com/profile.jpg",
//                "ghantech"
//        );
//
//        ResponseEntity<GuestResponse> response = guestController.registerGuest(request);
//
//        verify(guestService, times(1)).registerGuest(request);
//        assertEquals(200, response.getStatusCodeValue());
//    }
//
//    @Test
//    void whenUpdateGuest_thenGuestIsUpdated() {
//        String id = "mockId";
//        GuestUpdateRequest request = new GuestUpdateRequest(
//                "Jane",
//                "Doe",
//                "Smith",
//                "0987654321",
//                "http://example.com/profile2.jpg",
//                "ghantech"
//        );
//
//        ResponseEntity<GuestResponse> response = guestController.updateGuest(id, request);
//
//        verify(guestService, times(1)).updateGuest(id, request);
//        assertEquals(200, response.getStatusCodeValue());
//    }
//
//    @Test
//    void whenGetAllGuests_thenGuestsAreReturned() {
//        ResponseEntity<Iterable<GuestResponse>> response = guestController.getAllGuests();
//
//        verify(guestService, times(1)).getAllGuests();
//        assertEquals(200, response.getStatusCodeValue());
//    }
//
//    @Test
//    void whenGetGuest_thenGuestIsReturned() {
//        String id = "mockId";
//        GuestResponse guest = new GuestResponse();
//        when(guestService.getGuest(id)).thenReturn(guest);
//
//        ResponseEntity<GuestResponse> response = guestController.getGuest(id);
//
//        verify(guestService, times(1)).getGuest(id);
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals(guest, response.getBody());
//    }
//
//}
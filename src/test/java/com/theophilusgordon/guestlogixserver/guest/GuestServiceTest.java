package com.theophilusgordon.guestlogixserver.guest;

import com.theophilusgordon.guestlogixserver.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class GuestServiceTest {

    @Mock
    private GuestRepository guestRepository;

    @InjectMocks
    private GuestService guestService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenRegisterGuest_thenGuestIsCreated() {
        GuestRegisterRequest request = new GuestRegisterRequest(
                "John",
                "Doe",
                "Smith",
                "john@example.com",
                "1234567890",
                "http://example.com/profile.jpg",
                "ghantech"
        );

        guestService.registerGuest(request);

        verify(guestRepository, times(1)).save(any(Guest.class));
    }

    @Test
    void whenUpdateGuest_thenGuestIsUpdated() {
        String id = "mockId";
        GuestUpdateRequest request = new GuestUpdateRequest(
                "Jane",
                "Doe",
                "Smith",
                "0987654321",
                "http://example.com/profile2.jpg",
                "ghantech"
        );

        Guest guest = new Guest();
        when(guestRepository.findById(anyString())).thenReturn(Optional.of(guest));

        guestService.updateGuest(id, request);

        verify(guestRepository, times(1)).save(any(Guest.class));
    }

    @Test
    void whenGetAllGuests_thenGuestsAreReturned() {
        guestService.getAllGuests();

        verify(guestRepository, times(1)).findAll();
    }

    @Test
    void whenGetGuest_thenGuestIsReturned() {
        String id = "mockId";
        Guest guest = new Guest();
        when(guestRepository.findById(anyString())).thenReturn(Optional.of(guest));

        guestService.getGuest(id);

        verify(guestRepository, times(1)).findById(anyString());
    }

    @Test
    void whenDeleteGuest_thenGuestIsDeleted() {
        String id = "mockId";
        Guest guest = new Guest();
        when(guestRepository.findById(anyString())).thenReturn(Optional.of(guest));

        guestService.deleteGuest(id);

        verify(guestRepository, times(1)).deleteById(anyString());
    }

    @Test
    void whenGetGuest_thenNotFoundExceptionIsThrown() {
        String id = "mockId";
        when(guestRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> guestService.getGuest(id));
    }

    @Test
    void whenDeleteGuest_thenNotFoundExceptionIsThrown() {
        String id = "mockId";
        when(guestRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> guestService.deleteGuest(id));
    }
}
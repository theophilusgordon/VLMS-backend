package com.theophilusgordon.vlmsbackend.guest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theophilusgordon.vlmsbackend.exception.BadRequestException;
import com.theophilusgordon.vlmsbackend.exception.NotFoundException;
import com.theophilusgordon.vlmsbackend.utils.QRCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GuestService {
    private final GuestRepository guestRepository;
    private final ObjectMapper objectMapper;
    private final QRCodeService qrCodeService;

// TODO: Implement method to check in a guest with QR code details

    public Guest registerGuest(GuestRegisterRequest request){
        guestRepository.findByEmail(request.email())
            .ifPresent(guest -> {
                throw new BadRequestException("Email already exists");
            });

        Guest guest = objectMapper.convertValue(request, Guest.class);
        return guestRepository.save(guest);
    }

    public Page<Guest> getGuests(Pageable pageable){
        return guestRepository.findAll(pageable);
    }

    public Page<Guest> searchGuests(Pageable pageable, String query){
        return guestRepository.findAllByFullNameContaining(query, pageable);
    }

    public Guest getGuest(@PathVariable String id){
        return guestRepository.findById(UUID.fromString(id))
            .orElseThrow(() -> new NotFoundException("Guest", id));
    }

    public Long getTotalGuests(){
        return guestRepository.count();
    }
}

package com.theophilusgordon.guestlogixserver.guest;

import com.theophilusgordon.guestlogixserver.exception.BadRequestException;
import com.theophilusgordon.guestlogixserver.exception.NotFoundException;
import com.theophilusgordon.guestlogixserver.utils.QRCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@RequiredArgsConstructor
public class GuestService {
    private final GuestRepository guestRepository;
    private final QRCodeService qrCodeService;

    public Guest findByQrCode(byte[] qrCode){
        if(qrCode == null)
            throw new BadRequestException("QR Code is required");

        return guestRepository.findByQrCode(qrCode)
            .orElseThrow(() -> new NotFoundException("Guest", "QR Code"));
    }

    public GuestResponse registerGuest(GuestRegisterRequest request){
        if(guestRepository.existsByEmail(request.getEmail()))
            throw new BadRequestException("Email already exists");

        Guest guest = Guest.builder()
            .firstName(request.getFirstName())
            .middleName(request.getMiddleName())
            .lastName(request.getLastName())
            .email(request.getEmail())
            .phone(request.getPhone())
            .profilePhotoUrl(request.getProfilePhotoUrl())
            .company(request.getCompany())
            .build();
        guestRepository.save(guest);
        return this.buildGuestResponse(guest);
    }

    public GuestResponse updateGuest(@PathVariable String id, GuestUpdateRequest request){
        Guest guest = guestRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Guest", id));
        if(request.getFirstName() != null)
            guest.setFirstName(request.getFirstName());
        if(request.getMiddleName() != null)
            guest.setMiddleName(request.getMiddleName());
        if(request.getLastName() != null)
            guest.setLastName(request.getLastName());
        if(request.getPhone() != null)
            guest.setPhone(request.getPhone());
        if(request.getProfilePhotoUrl() != null)
            guest.setProfilePhotoUrl(request.getProfilePhotoUrl());
        if(request.getCompany() != null)
            guest.setCompany(request.getCompany());
        guestRepository.save(guest);

        return this.buildGuestResponse(guest);
    }

    public Page<GuestResponse> getGuests(int page, int size, String sort){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return guestRepository.findAll(pageable)
                .map(this::buildGuestResponse);
    }

    public Page<GuestResponse> searchGuests(String query, int page, int size, String sort){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return guestRepository.findAllByFullNameContaining(query, pageable)
                .map(this::buildGuestResponse);
    }

    public GuestResponse getGuest(@PathVariable String id){
        var guest =  guestRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Guest", id));
        return this.buildGuestResponse(guest);
    }

    public Long getTotalGuests(){
        return guestRepository.count();
    }

    private GuestResponse buildGuestResponse(Guest guest) {
        return GuestResponse.builder()
                .id(guest.getId())
                .firstName(guest.getFirstName())
                .middleName(guest.getMiddleName())
                .lastName(guest.getLastName())
                .email(guest.getEmail())
                .phone(guest.getPhone())
                .profilePhotoUrl(guest.getProfilePhotoUrl())
                .company(guest.getCompany())
                .build();
    }
}

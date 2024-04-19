package com.theophilusgordon.guestlogixserver.checkInOut;

import com.theophilusgordon.guestlogixserver.exception.NotFoundException;
import com.theophilusgordon.guestlogixserver.guest.GuestRepository;
import com.theophilusgordon.guestlogixserver.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CheckInOutService {
    private final CheckInOutRepository checkInOutRepository;
    private final GuestRepository guestRepository;
    private final UserRepository hostRepository;

    public void checkIn(CheckInRequest request) {
        var checkInOut = new CheckInOut();
        checkInOut.setCheckIn(LocalDateTime.now());
        var guest = guestRepository.findById(request.getGuestId()).orElseThrow(() -> new NotFoundException("Guest", request.getGuestId()));
        checkInOut.setGuest(guest);
        var host = hostRepository.findById(request.getHostId()).orElseThrow(() -> new NotFoundException("User", request.getHostId()));
        checkInOut.setHost(host);
        checkInOutRepository.save(checkInOut);

    }

    public void checkOut(Integer id) {
        checkInOutRepository.findById(id).orElseThrow(() -> new NotFoundException("CheckInOut", String.valueOf(id)));
        var checkInOut = new CheckInOut();
        checkInOut.setCheckOut(LocalDateTime.now());
    }
}

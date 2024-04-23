package com.theophilusgordon.guestlogixserver.checkIn;

import com.theophilusgordon.guestlogixserver.exception.BadRequestException;
import com.theophilusgordon.guestlogixserver.exception.NotFoundException;
import com.theophilusgordon.guestlogixserver.guest.Guest;
import com.theophilusgordon.guestlogixserver.guest.GuestRepository;
import com.theophilusgordon.guestlogixserver.guest.GuestResponse;
import com.theophilusgordon.guestlogixserver.user.User;
import com.theophilusgordon.guestlogixserver.user.UserRepository;
import com.theophilusgordon.guestlogixserver.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class CheckInService {
    private final CheckInRepository checkInRepository;
    private final GuestRepository guestRepository;
    private final UserRepository hostRepository;

    public CheckInResponse checkIn(CheckInRequest request) {
        var checkIn = new CheckIn();
        checkIn.setCheckInDateTime(LocalDateTime.now());
        Guest guest = guestRepository.findById(request.getGuestId()).orElseThrow(() -> new NotFoundException("Guest", request.getGuestId()));
        checkIn.setGuest(guest);
        User host = hostRepository.findById(request.getHostId()).orElseThrow(() -> new NotFoundException("User", request.getHostId()));
        checkIn.setHost(host);
        checkInRepository.save(checkIn);
        return this.buildCheckInResponse(checkIn, guest, host);
    }

    public CheckInResponse checkOut(Integer id) {
        var checkIn = checkInRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("CheckInOut", String.valueOf(id)));
        checkIn.setCheckOutDateTime(LocalDateTime.now());
        checkInRepository.save(checkIn);
        return this.buildCheckInResponse(checkIn, checkIn.getGuest(), checkIn.getHost());
    }

    public Iterable<CheckInResponse> getCheckIns() {
        var checkIns = checkInRepository.findAll();
        return checkIns.stream()
                .map(checkIn -> this.buildCheckInResponse(checkIn, checkIn.getGuest(), checkIn.getHost()))
                .toList();
    }

    public CheckInResponse getCheckIn(Integer id) {
        var checkIn = checkInRepository.findById(id).orElseThrow(() -> new NotFoundException("CheckInOut", String.valueOf(id)));
        return this.buildCheckInResponse(checkIn, checkIn.getGuest(), checkIn.getHost());
    }

    public Iterable<CheckInResponse> getCheckInsByGuest(String guestId) {
        if(!guestRepository.existsById(guestId))
            throw new NotFoundException("Guest", guestId);

        Iterable<CheckIn> checkIns = checkInRepository.findByGuestId(guestId);
        return StreamSupport.stream(checkIns.spliterator(), false)
                .map(checkIn -> this.buildCheckInResponse(checkIn, checkIn.getGuest(), checkIn.getHost()))
                .toList();
    }

    public Iterable<CheckInResponse> getCheckInsByHost(String hostId) {
        if(!hostRepository.existsById(hostId))
            throw new NotFoundException("Host", hostId);

        Iterable<CheckIn> checkIns = checkInRepository.findByHostId(hostId);
        return StreamSupport.stream(checkIns.spliterator(), false)
                .map(checkIn -> this.buildCheckInResponse(checkIn, checkIn.getGuest(), checkIn.getHost()))
                .toList();
    }

    public Iterable<CheckInResponse> getCheckInsByCheckInDate(String checkInDate) {
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(checkInDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Invalid date format. Please use the ISO 8601 date time format: yyyy-MM-dd'T'HH:mm:ss");
        }
        var checkIns = checkInRepository.findByCheckInDateTime(dateTime);
        return StreamSupport.stream(checkIns.spliterator(), false)
                .map(checkIn -> this.buildCheckInResponse(checkIn, checkIn.getGuest(), checkIn.getHost()))
                .toList();
    }

    public Iterable<CheckInResponse> getCheckInsByPeriod(String start, String end) {
        Pair<LocalDateTime, LocalDateTime> dates = validateAndParseDates(start, end);
        var checkIns = checkInRepository.findByCheckInDateTimeBetween(dates.getFirst(), dates.getSecond());
        return StreamSupport.stream(checkIns.spliterator(), false)
                .map(checkIn -> this.buildCheckInResponse(checkIn, checkIn.getGuest(), checkIn.getHost())).
                toList();
    }

    public Iterable<CheckInResponse> getCheckInsByHostAndPeriod(String hostId, String start, String end) {
        if(!hostRepository.existsById(hostId))
            throw new NotFoundException("Host", hostId);

        Pair<LocalDateTime, LocalDateTime> dates = validateAndParseDates(start, end);
        var checkIns = checkInRepository.findByHostIdAndCheckInDateTimeBetween(hostId, dates.getFirst(), dates.getSecond());
        return StreamSupport.stream(checkIns.spliterator(), false)
                .map(checkIn -> this.buildCheckInResponse(checkIn, checkIn.getGuest(), checkIn.getHost())).
                toList();
    }

    private CheckInResponse buildCheckInResponse(CheckIn checkIn, Guest guest, User host) {
        if(guest == null || host == null)
            throw new BadRequestException("Guest and Host must be provided");
        return CheckInResponse.builder()
                .id(checkIn.getId())
                .checkInDateTime(checkIn.getCheckInDateTime())
                .checkOutDateTime(checkIn.getCheckOutDateTime())
                .guest(GuestResponse.builder()
                        .id(guest.getId())
                        .firstName(guest.getFirstName())
                        .middleName(guest.getMiddleName())
                        .lastName(guest.getLastName())
                        .phone(guest.getPhone())
                        .email(guest.getEmail())
                        .profilePhotoUrl(guest.getProfilePhotoUrl())
                        .build())
                .host(UserResponse.builder()
                        .id(host.getId())
                        .firstName(host.getFirstName())
                        .middleName(host.getMiddleName())
                        .lastName(host.getLastName())
                        .phone(host.getPhone())
                        .email(host.getEmail())
                        .profilePhotoUrl(host.getProfilePhotoUrl())
                        .build())
                .build();
    }

    private Pair<LocalDateTime, LocalDateTime> validateAndParseDates(String start, String end) {
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;
        try {
            startDateTime = LocalDateTime.parse(start, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            endDateTime = LocalDateTime.parse(end, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Invalid date format. Please use the ISO 8601 date time format: yyyy-MM-dd'T'HH:mm:ss");
        }

        if (startDateTime.isAfter(endDateTime)) {
            throw new BadRequestException("Start date cannot be after end date");
        }

        return Pair.of(startDateTime, endDateTime);
    }
}

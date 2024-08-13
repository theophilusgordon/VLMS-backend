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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class VisitService {
    private final VisitRepository checkInRepository;
    private final GuestRepository guestRepository;
    private final UserRepository hostRepository;
    private final QRCodeService qrCodeService;
    private final EmailService emailService;


    public VisitResponse checkIn(VisitRequest request) {
        var checkin = new Visit();
        checkin.setCheckInDateTime(LocalDateTime.now());

        Guest guest = guestRepository.findById(UUID.fromString(request.guestId()))
                .orElseThrow(() -> new NotFoundException("Guest", request.guestId()));
        checkin.setGuest(guest);
        if(checkInRepository.existsByGuestIdAndCheckOutDateTimeIsNull(guest.getId()))
            throw new BadRequestException(ExceptionConstants.GUEST_ALREADY_CHECKED_IN);

        User host = hostRepository.findById(UUID.fromString(request.hostId()))
                .orElseThrow(() -> new NotFoundException("User", request.hostId()));

        checkin.setHost(host);
        checkin.setQrCode(qrCodeService.generateQRCodeImage(String.valueOf(guest.getId())));
        checkInRepository.save(checkin);

        emailService.sendCheckinSuccessEmail(
                guest.getEmail(),
                guest.getFirstName(),
                checkin.getQrCode()
        );

        emailService.sendCheckinNotificationEmail(
                host.getEmail(),
                host.getFirstName(),
                guest,
                checkin.getCheckInDateTime()
        );

        return this.buildCheckInResponse(checkin, guest, host);
    }

    public VisitResponse checkOut(Integer id) {
        var checkIn = checkInRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("CheckInOut", String.valueOf(id)));
        if(checkIn.getCheckOutDateTime() != null)
            throw new BadRequestException(ExceptionConstants.GUEST_ALREADY_CHECKED_OUT);

        checkIn.setCheckOutDateTime(LocalDateTime.now());
        checkInRepository.save(checkIn);

        emailService.sendCheckoutSuccessEmail(
                checkIn.getGuest().getEmail(),
                checkIn.getGuest().getFirstName(),
                checkIn.getCheckOutDateTime()
        );

        emailService.sendCheckoutNotificationEmail(
                checkIn.getHost().getEmail(),
                checkIn.getHost().getFirstName(),
                checkIn.getGuest(),
                checkIn.getCheckOutDateTime()
        );
        return this.buildCheckInResponse(checkIn, checkIn.getGuest(), checkIn.getHost());
    }

    public Page<VisitResponse> getCheckIns(Pageable pageable) {
        Page<Visit> checkIns = checkInRepository.findAll(pageable);
        return checkIns.map(visit -> this.buildCheckInResponse(visit, visit.getGuest(), visit.getHost()));
    }

    public VisitResponse getCheckIn(Integer id) {
        var checkIn = checkInRepository.findById(id).orElseThrow(() -> new NotFoundException("CheckInOut", String.valueOf(id)));
        return this.buildCheckInResponse(checkIn, checkIn.getGuest(), checkIn.getHost());
    }

    public List<VisitResponse> getCheckInsByGuest(UUID guestId) {
        if(!guestRepository.existsById(guestId))
            throw new NotFoundException("Guest", guestId);

        List<Visit> checkIns = checkInRepository.findByGuestId(guestId);
        return checkIns.stream()
                .map(visit -> this.buildCheckInResponse(visit, visit.getGuest(), visit.getHost()))
                .toList();
    }

    public List<VisitResponse> getCheckInsByHost(UUID hostId) {
        if(!hostRepository.existsById(hostId))
            throw new NotFoundException("Host", hostId);

        List<Visit> checkIns = checkInRepository.findByHostId(hostId);
        return checkIns.stream()
                .map(visit -> this.buildCheckInResponse(visit, visit.getGuest(), visit.getHost()))
                .toList();
    }

    public List<VisitResponse> getCheckInsByCheckInDate(LocalDateTime checkInDate) {
        List<Visit> checkIns = checkInRepository.findByCheckInDateTime(checkInDate);
        return checkIns.stream()
                .map(visit -> this.buildCheckInResponse(visit, visit.getGuest(), visit.getHost()))
                .toList();
    }

    public List<VisitResponse> getCheckInsByPeriod(LocalDateTime start, LocalDateTime end) {
        var checkIns = checkInRepository.findByCheckInDateTimeBetween(start, end);
        return checkIns.stream()
                .map(visit -> this.buildCheckInResponse(visit, visit.getGuest(), visit.getHost())).
                toList();
    }

    public List<VisitResponse> getCheckInsByHostAndPeriod(UUID hostId, LocalDateTime start, LocalDateTime end) {
        if(!hostRepository.existsById(hostId))
            throw new NotFoundException("Host", hostId);

        var checkIns = checkInRepository.findByHostIdAndCheckInDateTimeBetween(hostId, start, end);
        return checkIns.stream()
                .map(visit -> this.buildCheckInResponse(visit, visit.getGuest(), visit.getHost())).
                toList();
    }

    public List<VisitResponse> getCurrentGuests() {
        var checkIns = checkInRepository.findCurrentGuests();
        return checkIns.stream()
                .map(visit -> this.buildCheckInResponse(visit, visit.getGuest(), visit.getHost()))
                .toList();
    }

    public Integer getTotalVisits(){
        return checkInRepository.findAll().size();
    }

    public Integer getTotalCurrentGuests(){
        return checkInRepository.countByCheckInDateTimeIsNotNullAndCheckOutDateTimeIsNull().intValue();
    }

    public Integer getTotalVisitsBetween(LocalDateTime start, LocalDateTime end){
        return checkInRepository.findByCheckInDateTimeBetween(start, end).size();
    }

    private VisitResponse buildCheckInResponse(Visit checkIn, Guest guest, User host) {
        if(guest == null || host == null)
            throw new BadRequestException("Guest and Host must be provided");
        return VisitResponse.builder()
                .id(checkIn.getId())
                .checkInDateTime(checkIn.getCheckInDateTime())
                .checkOutDateTime(checkIn.getCheckOutDateTime())
                .guest(Guest.builder()
                        .id(guest.getId())
                        .firstName(guest.getFirstName())
                        .middleName(guest.getMiddleName())
                        .lastName(guest.getLastName())
                        .phone(guest.getPhone())
                        .email(guest.getEmail())
                        .profilePhoto(guest.getProfilePhoto())
                        .build())
                .host(User.builder()
                        .id(host.getId())
                        .email(host.getEmail())
                        .firstName(host.getFirstName())
                        .middleName(host.getMiddleName())
                        .lastName(host.getLastName())
                        .phone(host.getPhone())
                        .email(host.getEmail())
                        .department(host.getDepartment())
                        .profilePhoto(host.getProfilePhoto())
                        .build())
                .build();
    }
}

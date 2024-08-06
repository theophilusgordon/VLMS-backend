package com.theophilusgordon.vlmsbackend.visit;

import com.google.zxing.WriterException;
import com.theophilusgordon.vlmsbackend.exception.BadRequestException;
import com.theophilusgordon.vlmsbackend.exception.NotFoundException;
import com.theophilusgordon.vlmsbackend.guest.Guest;
import com.theophilusgordon.vlmsbackend.guest.GuestRepository;
import com.theophilusgordon.vlmsbackend.user.User;
import com.theophilusgordon.vlmsbackend.user.UserRepository;
import com.theophilusgordon.vlmsbackend.utils.email.EmailService;
import com.theophilusgordon.vlmsbackend.utils.QRCodeService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.io.IOException;
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


    public VisitResponse checkIn(VisitRequest request) throws IOException, WriterException, MessagingException {
        var checkin = new Visit();
        checkin.setCheckInDateTime(LocalDateTime.now());
        Guest guest = guestRepository.findById(UUID.fromString(request.guestId())).orElseThrow(() -> new NotFoundException("Guest", request.guestId()));
        checkin.setGuest(guest);
        User host = hostRepository.findById(UUID.fromString(request.hostId())).orElseThrow(() -> new NotFoundException("User", request.hostId()));
        checkin.setHost(host);
        checkin.setQrCode(qrCodeService.generateQRCodeImage(String.valueOf(guest)));
        checkInRepository.save(checkin);

        emailService.sendCheckinSuccessEmail(
                guest.getEmail(),
                "Successful Check-In at VLMS - Your QR Code for Future Visits",
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
        checkIn.setCheckOutDateTime(LocalDateTime.now());
        checkInRepository.save(checkIn);
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

    public List<VisitResponse> getCheckInsByGuest(String guestId) {
        if(!guestRepository.existsById(UUID.fromString(guestId)))
            throw new NotFoundException("Guest", guestId);

        List<Visit> checkIns = checkInRepository.findByGuestId(UUID.fromString(guestId));
        return checkIns.stream()
                .map(visit -> this.buildCheckInResponse(visit, visit.getGuest(), visit.getHost()))
                .toList();
    }

    public List<VisitResponse> getCheckInsByHost(String hostId) {
        if(!hostRepository.existsById(UUID.fromString(hostId)))
            throw new NotFoundException("Host", hostId);

        List<Visit> checkIns = checkInRepository.findByHostId(UUID.fromString(hostId));
        return checkIns.stream()
                .map(visit -> this.buildCheckInResponse(visit, visit.getGuest(), visit.getHost()))
                .toList();
    }

    public List<VisitResponse> getCheckInsByCheckInDate(String checkInDate) {
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(checkInDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Invalid date format. Please use the ISO 8601 date time format: yyyy-MM-dd'T'HH:mm:ss");
        }
        var checkIns = checkInRepository.findByCheckInDateTime(dateTime);
        return checkIns.stream()
                .map(visit -> this.buildCheckInResponse(visit, visit.getGuest(), visit.getHost()))
                .toList();
    }

    public List<VisitResponse> getCheckInsByPeriod(String start, String end) {
        Pair<LocalDateTime, LocalDateTime> dates = validateAndParseDates(start, end);
        var checkIns = checkInRepository.findByCheckInDateTimeBetween(dates.getFirst(), dates.getSecond());
        return checkIns.stream()
                .map(visit -> this.buildCheckInResponse(visit, visit.getGuest(), visit.getHost())).
                toList();
    }

    public List<VisitResponse> getCheckInsByHostAndPeriod(String hostId, String start, String end) {
        if(!hostRepository.existsById(UUID.fromString(hostId)))
            throw new NotFoundException("Host", hostId);

        Pair<LocalDateTime, LocalDateTime> dates = validateAndParseDates(start, end);
        var checkIns = checkInRepository.findByHostIdAndCheckInDateTimeBetween(UUID.fromString(hostId), dates.getFirst(), dates.getSecond());
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

    public Integer getTotalVisitsBetween(String start, String end){
        Pair<LocalDateTime, LocalDateTime> dates = validateAndParseDates(start, end);
        return (int) StreamSupport.stream(checkInRepository.findByCheckInDateTimeBetween(dates.getFirst(), dates.getSecond()).spliterator(), false).count();
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
                        .profilePhotoUrl(guest.getProfilePhotoUrl())
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

package com.theophilusgordon.vlmsbackend.utils.email;

import com.theophilusgordon.vlmsbackend.guest.Guest;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailDetails {
    private String recipient;
    private EmailTemplate template;
    private String otp;
    private String activationUrl;
    private String loginUrl;
    private String resetPasswordUrl;
    private String subject;
    private String hostName;
    private String guestName;
    private LocalDateTime checkInTime;
    private byte[] qrCode;
    private String guestPhone;
    private String guestCompany;
    private String name;
}

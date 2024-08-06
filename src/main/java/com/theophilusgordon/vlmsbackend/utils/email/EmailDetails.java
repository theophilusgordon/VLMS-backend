package com.theophilusgordon.vlmsbackend.utils.email;

import lombok.*;

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
    private String subject;
    private String type;
    private String name;
    private String reason;
    private String date;
}

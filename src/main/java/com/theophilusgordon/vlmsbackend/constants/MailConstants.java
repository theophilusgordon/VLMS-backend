package com.theophilusgordon.vlmsbackend.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MailConstants {
    public static final String REQUEST_RESET_PASSWORD_SUBJECT = "Password Reset Request for Your Guest Logix Account";
    public static final String INVITATION_SUBJECT = "Invitation to Join Guest Logix as a Host";
    public static final String PASSWORD_RESET_SUCCESS_SUBJECT = "Your password has been reset successfully";
}

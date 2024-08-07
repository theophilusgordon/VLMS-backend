package com.theophilusgordon.vlmsbackend.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailConstants {
    public static final String REQUEST_RESET_PASSWORD_SUBJECT = "VLMS - Reset Password";
    public static final String PASSWORD_RESET_SUCCESS_SUBJECT = "VLMS - Password Reset Successful";
    public static final String CHECKIN_NOTIFICATION_SUBJECT = "VLMS - Your Guest has Arrived";
    public static final String CHECKIN_SUCCESS_SUBJECT = "VLMS - Welcome";
    public static final String CHECKOUT_NOTIFICATION_SUBJECT = "VLMS - Your Guest is Leaving";
    public static final String CHECKOUT_SUCCESS_SUBJECT = "VLMS - Goodbye";
    public static final String ACTIVATE_ACCOUNT_SUBJECT = "VLMS - Activate Your Account";
    public static final String ACCOUNT_ACTIVATION_SUBJECT = "VLMS - Account Account Setup is Complete";
    public static final String ACCOUNT_DEACTIVATION_SUBJECT = "VLMS - Your Account Has Been Deactivated";
}

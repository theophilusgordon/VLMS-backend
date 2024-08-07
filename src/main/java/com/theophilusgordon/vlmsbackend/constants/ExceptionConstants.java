package com.theophilusgordon.vlmsbackend.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionConstants {
    public static final String INVALID_TOKEN = "Invalid token";
    public static final String INVALID_ACTIVATION_CODE = "Invalid activation code";
    public static final String ACCESS_DENIED = "Access denied";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String USER_ALREADY_EXISTS = "User already exists with email: ";
    public static final String INVALID_EMAIL = "Invalid email format";
    public static final String INVALID_PASSWORD = "Password must be at least 8 characters, with at least one uppercase letter, one lowercase letter, one number, and one special character";
    public static final String BAD_CREDENTIALS = "The username or password you provided is incorrect.";
    public static final String PAGE_NOT_FOUND = "Page not found";
    public static final String INVALID_ROLE = "Role must either be 'admin' or 'host'";
    public static final String PASSWORDS_MISMATCH = "Passwords do not match";
    public static final String INCORRECT_PASSWORD = "Incorrect password";
    public static final String INVALID_PHONE_NUMBER = "Phone number is not valid";
    public static final String NAME_LIMIT = "Firstname, Middlename or Lastname cannot be more than 50 characters";
    public static final String USER_NOT_INVITED = "Make sure you have been invited by an admin to activate your account. User not found with email: ";
    public static final String USER_ALREADY_ACTIVATED = "User account already activated with email: ";
    public static final String EMAIL_SEND_FAILURE = "Failed to send email";
    public static final String EXPIRED_TOKEN = "Token has expired";
    public static final String GUEST_ALREADY_CHECKED_IN = "Guest is already checked in";
}

package com.theophilusgordon.vlmsbackend.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionConstants {
    public static final String INVALID_TOKEN = "Invalid token";
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
}

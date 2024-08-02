package com.theophilusgordon.vlmsbackend.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionConstants {
    public static final String INVALID_CREDENTIALS = "Invalid credentials";
    public static final String INVALID_TOKEN = "Invalid token";
    public static final String ACCESS_DENIED = "Access denied";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String USER_ALREADY_EXISTS = "User already exists";
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
    public static final String INVALID_EMAIL = "Invalid email";
    public static final String INVALID_PASSWORD = "Invalid password";
    public static final String BAD_CREDENTIALS = "The username or password you provided is incorrect.";

}

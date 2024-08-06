package com.theophilusgordon.vlmsbackend.auth;

import com.theophilusgordon.vlmsbackend.constants.ExceptionConstants;
import com.theophilusgordon.vlmsbackend.constants.Patterns;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AccountActivationRequest(
        @NotBlank(message = "First name is required")
        @Size(max = 50, message = ExceptionConstants.NAME_LIMIT)
        String firstName,
        @Size(max = 50, message = ExceptionConstants.NAME_LIMIT)
        String middleName,
        @NotBlank(message = "Last name is required")
        @Size(max = 50, message = ExceptionConstants.NAME_LIMIT)
        String lastName,
        @NotBlank(message = "Phone is required")
        @Pattern(regexp = Patterns.PHONE_NUMBER, message = ExceptionConstants.INVALID_PHONE_NUMBER)
        String phone,
        @NotBlank(message = "Email is required")
        @Email
        String email,
        String profilePhotoUrl,
        @NotBlank(message = "Password is required")
        @Pattern(regexp = Patterns.PASSWORD, message = ExceptionConstants.INVALID_PASSWORD)
        String password,
        @NotBlank(message = "Confirm password is required")
        @Pattern(regexp = Patterns.PASSWORD, message = ExceptionConstants.INVALID_PASSWORD)
        String confirmPassword,
        @NotBlank(message = "OTP is required")
        String otp
) {
}
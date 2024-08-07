package com.theophilusgordon.vlmsbackend.user;

import com.theophilusgordon.vlmsbackend.constants.ExceptionConstants;
import com.theophilusgordon.vlmsbackend.constants.Patterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PasswordResetRequest(
        @NotBlank
        @Pattern(regexp = Patterns.EMAIL, message = ExceptionConstants.INVALID_EMAIL)
        String email,
        @NotBlank(message = "Password is required")
        @Pattern(regexp = Patterns.PASSWORD, message = ExceptionConstants.INVALID_PASSWORD)
        String password,
        @NotBlank(message = "Confirm password is required")
        String confirmPassword,
        @NotBlank(message = "OTP is required")
        String otp
) {
}

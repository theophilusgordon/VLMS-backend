package com.theophilusgordon.vlmsbackend.user;

import com.theophilusgordon.vlmsbackend.constants.ExceptionConstants;
import com.theophilusgordon.vlmsbackend.constants.Patterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PasswordChangeRequest(
        @NotBlank(message = "Current password is required")
        String currentPassword,
        @NotBlank
        @Pattern(regexp = Patterns.PASSWORD, message = ExceptionConstants.INVALID_PASSWORD)
        String newPassword,
        @NotBlank(message = "Confirm password is required")
        String confirmationPassword
) {
}

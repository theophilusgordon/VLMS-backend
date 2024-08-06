package com.theophilusgordon.vlmsbackend.user;

import com.theophilusgordon.vlmsbackend.constants.ExceptionConstants;
import com.theophilusgordon.vlmsbackend.constants.Patterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PasswordRequestResetRequest(
        @NotBlank(message = "Email is required")
        @Pattern(regexp = Patterns.EMAIL, message = ExceptionConstants.INVALID_EMAIL)
        String email
) {
}

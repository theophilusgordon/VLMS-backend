package com.theophilusgordon.vlmsbackend.user;

import com.theophilusgordon.vlmsbackend.constants.ExceptionConstants;
import com.theophilusgordon.vlmsbackend.constants.Patterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserInviteRequest(
        @NotBlank(message = "Email is required")
        @Pattern(regexp = Patterns.EMAIL,message = ExceptionConstants.INVALID_EMAIL)
        String email,
        @NotBlank(message = "Role is required")
        @Pattern(regexp = Patterns.ROLE, message = ExceptionConstants.INVALID_ROLE)
        String role
) {
}

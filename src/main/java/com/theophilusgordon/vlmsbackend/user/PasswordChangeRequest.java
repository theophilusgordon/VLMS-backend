package com.theophilusgordon.vlmsbackend.user;

import com.theophilusgordon.vlmsbackend.constants.ExceptionConstants;
import com.theophilusgordon.vlmsbackend.constants.Patterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PasswordChangeRequest {
    @NotBlank(message = "Current password is required")
    private String currentPassword;
    @NotBlank
    @Pattern(regexp = Patterns.PASSWORD, message = ExceptionConstants.INVALID_PASSWORD)
    private String newPassword;
    @NotBlank(message = "Confirm password is required")
    private String confirmationPassword;
}

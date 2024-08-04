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
public class PasswordResetRequest {
    @NotBlank(message = "Password is required")
    @Pattern(regexp = Patterns.PASSWORD, message = ExceptionConstants.INVALID_PASSWORD)
    private String password;
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
}

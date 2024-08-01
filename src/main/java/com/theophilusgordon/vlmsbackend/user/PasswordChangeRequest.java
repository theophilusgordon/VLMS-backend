package com.theophilusgordon.vlmsbackend.user;

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
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Password must be at least 8 characters, with at least one uppercase letter, one lowercase letter, one number, and one special character")
    private String newPassword;
    @NotBlank(message = "Confirm password is required")
    private String confirmationPassword;
}

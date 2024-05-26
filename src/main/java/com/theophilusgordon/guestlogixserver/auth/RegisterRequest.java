package com.theophilusgordon.guestlogixserver.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "First name is required")
    private String firstName;
    private String middleName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    @NotBlank(message = "Phone is required")
    private String phone;
    @NotBlank(message = "Email is required")
    @Email
    private String email;
    private String profilePhotoUrl;
    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$", message = "Password must contain at least 8 characters, one uppercase, one lowercase and one number")
    private String password;
    @NotBlank(message = "Confirm password is required")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$", message = "Password must contain at least 8 characters, one uppercase, one lowercase and one number")
    private String confirmPassword;
}

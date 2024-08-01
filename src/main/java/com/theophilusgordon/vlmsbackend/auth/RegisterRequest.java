package com.theophilusgordon.vlmsbackend.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @Size(max = 50, message = "First name must be less than 50 characters")
    private String firstName;
    @Size(max = 50, message = "Middle name must be less than 50 characters")
    private String middleName;
    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must be less than 50 characters")
    private String lastName;
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\+?[0-9. ()-]{10,25}$", message = "Phone number is not valid")
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

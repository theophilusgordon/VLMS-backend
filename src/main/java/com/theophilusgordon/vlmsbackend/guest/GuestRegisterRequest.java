package com.theophilusgordon.vlmsbackend.guest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record GuestRegisterRequest(
        @NotBlank(message = "First name is required")
        String firstName,
        String middleName,
        @NotBlank(message = "Last name is required")
        String lastName,
        @NotBlank(message = "Email is required")
        @Email
        String email,
        @NotBlank(message = "Phone is required")
        String phone,
        String profilePhotoUrl,
        @NotBlank(message = "Company is required")
        String company
) {
}

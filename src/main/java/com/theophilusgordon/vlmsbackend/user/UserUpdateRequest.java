package com.theophilusgordon.vlmsbackend.user;

import com.theophilusgordon.vlmsbackend.constants.Patterns;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

    public record UserUpdateRequest(
        @Size(max = 50, message = "First name must be less than 50 characters")
        String firstName,
        @Size(max = 50, message = "Middle name must be less than 50 characters")
        String middleName,
        @Size(max = 50, message = "Last name must be less than 50 characters")
        String lastName,
        @Pattern(regexp = Patterns.PHONE_NUMBER, message = "Phone number is not valid")
        String phone
    ) {
}

package com.theophilusgordon.vlmsbackend.user;

import com.theophilusgordon.vlmsbackend.constants.Patterns;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

    @Getter
   @Setter
   @Builder
    public class UserUpdateRequest {
        @Size(max = 50, message = "First name must be less than 50 characters")
        private String firstName;
        @Size(max = 50, message = "Middle name must be less than 50 characters")
        private String middleName;
        @Size(max = 50, message = "Last name must be less than 50 characters")
        private String lastName;
        @Pattern(regexp = Patterns.PHONE_NUMBER_PATTERN, message = "Phone number is not valid")
        private String phone;
        private String profilePhotoUrl;
}

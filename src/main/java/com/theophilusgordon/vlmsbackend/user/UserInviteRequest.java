package com.theophilusgordon.vlmsbackend.user;

import com.theophilusgordon.vlmsbackend.constants.Patterns;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserInviteRequest {
    @NotBlank(message = "Email is required")
    @Email
    private String email;
    @NotBlank(message = "Role is required")
    @Pattern(regexp = Patterns.ROLE_PATTERN, message = "Invalid role")
    private String role;
}

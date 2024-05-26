package com.theophilusgordon.guestlogixserver.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserInviteRequest {
    @NotBlank(message = "Email is rewuired")
    @Email
    private String email;
    @NotBlank(message = "Company is required")
    private String company;
    @NotBlank(message = "Role is required")
    private String role;
}

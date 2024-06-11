package com.theophilusgordon.guestlogixserver.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PasswordRequestResetRequest {
    @NotBlank(message = "Email is required")
    @Email
    private String email;
}

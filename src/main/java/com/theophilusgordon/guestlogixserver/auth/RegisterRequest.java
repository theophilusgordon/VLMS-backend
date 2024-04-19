package com.theophilusgordon.guestlogixserver.auth;

import com.theophilusgordon.guestlogixserver.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String firstName;
    private String middleName;
    private String lastName;
    private String phone;
    private String email;
    private String profilePhotoUrl;
    private String password;
    private String role;
}

package com.theophilusgordon.guestlogixserver.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateUserRequest {
    private String firstName;
    private String middleName;
    private String lastName;
    private String phone;
    private String email;
    private String profilePhotoUrl;
    private String password;
    private String role;
}

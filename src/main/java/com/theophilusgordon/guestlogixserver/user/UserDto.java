package com.theophilusgordon.guestlogixserver.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private String id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String phone;
    private String email;
    private String profilePhotoUrl;
    private String company;
    private Role role;
}

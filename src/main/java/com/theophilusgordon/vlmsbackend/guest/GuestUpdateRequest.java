package com.theophilusgordon.vlmsbackend.guest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuestUpdateRequest {
    private String firstName;
    private String middleName;
    private String lastName;
    private String phone;
    private String profilePhotoUrl;
    private String company;
}

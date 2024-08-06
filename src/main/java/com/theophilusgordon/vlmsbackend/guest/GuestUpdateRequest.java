package com.theophilusgordon.vlmsbackend.guest;

public record GuestUpdateRequest(
        String firstName,
        String middleName,
        String lastName,
        String phone,
        String profilePhotoUrl,
        String company
) {
}

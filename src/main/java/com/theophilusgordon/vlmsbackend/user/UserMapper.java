package com.theophilusgordon.vlmsbackend.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static User userUpdateRequestToUser(User user, UserUpdateRequest request){
        if(request.firstName() != null)
            user.setFirstName(request.firstName());
        if(request.middleName() != null)
            user.setMiddleName(request.middleName());
        if(request.lastName() != null)
            user.setLastName(request.lastName());
        if(request.phone() != null)
            user.setPhone(request.phone());
        return user;
    }
}

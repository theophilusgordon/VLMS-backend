package com.theophilusgordon.vlmsbackend.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthConstants {
    public static final String ALL_USER_AUTHORIZATION = "hasAnyAuthority('ROLE_ADMIN', 'GUEST', 'HOST')";
    public static final String ADMIN_HOST_AUTHORIZATION = "hasAnyAuthority('ADMIN', 'USER')";
    public static final String ADMIN_AUTHORIZATION = "hasAuthority('ADMIN')";

}

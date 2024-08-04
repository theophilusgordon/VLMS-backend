package com.theophilusgordon.vlmsbackend.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Patterns {
    public static final String PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    public static final String PHONE_NUMBER = "^\\+(?:[0-9] ?){6,14}[0-9]$";
    public static final String ROLE = "(?i)USER|ADMIN|HOST";
    public static final String EMAIL = "^(.+)@(.+)$";
}

package com.theophilusgordon.vlmsbackend.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN,
    HOST,
}

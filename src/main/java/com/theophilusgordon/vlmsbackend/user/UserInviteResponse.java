package com.theophilusgordon.vlmsbackend.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserInviteResponse {
    @JsonProperty("id")
    private String id;
    @JsonProperty("firstName")
    private String email;
    @JsonProperty("company")
    private String company;
    @JsonProperty("role")
    private String role;
    @JsonProperty("status")
    private Status status;
    @JsonProperty("inviteToken")
    private String inviteToken;
}

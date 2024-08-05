package com.theophilusgordon.vlmsbackend.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.theophilusgordon.vlmsbackend.token.Token;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails, Principal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String phone;
    private String email;
    private String profilePhotoUrl;
    private String department;
    private String position;
    private Boolean accountLocked = false;
    @Getter
    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Status status;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<Token> tokens;

    @JsonProperty("fullName")
    public String getFullName() {
        return String.format("%s %s %s", firstName, middleName, lastName);
    }

    @JsonProperty("initials")
    public String getInitials() {
        return String.format("%s%s", firstName.charAt(0), lastName.charAt(0));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    @JsonIgnore
    public String getName() {
        return email;
    }
}
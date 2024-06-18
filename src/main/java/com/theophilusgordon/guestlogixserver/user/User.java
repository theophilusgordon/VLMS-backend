package com.theophilusgordon.guestlogixserver.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.theophilusgordon.guestlogixserver.token.Token;
import com.theophilusgordon.guestlogixserver.token.TokenType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    @JsonProperty("id")
    private String id;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("middleName")
    private String middleName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("email")
    private String email;
    @JsonProperty("profilePhotoUrl")
    private String profilePhotoUrl;
    private String department;
    @JsonProperty("department")
    private String position;
    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    @JsonProperty("role")
    private Role role;

    @Enumerated(EnumType.STRING)
    @JsonProperty("status")
    private Status status;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

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
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

package com.theophilusgordon.guestlogixserver.guest;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Guest {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    @JsonProperty("id")
    private String id;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("middleName")
    private String middleName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("email")
    private String email;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("profilePhotoUrl")
    private String profilePhotoUrl;
    @JsonProperty("company")
    private String company;

    @Lob
    private byte[] qrCode;

    @JsonProperty("fullName")
    public String getFullName() {
        return String.format("%s %s %s", firstName, middleName, lastName);
    }

    @JsonProperty("initials")
    public String getInitials() {
        return String.format("%s%s", firstName.charAt(0), lastName.charAt(0));
    }
}

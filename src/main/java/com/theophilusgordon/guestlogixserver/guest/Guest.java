package com.theophilusgordon.guestlogixserver.guest;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Data
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
    private String id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String phone;
    private String profilePhotoUrl;
    private String company;

    public String getFullName() {
        return String.format("%s %s %s", firstName, middleName, lastName);
    }

    public String getInitials() {
        return String.format("%s%s", firstName.charAt(0), lastName.charAt(0));
    }
}

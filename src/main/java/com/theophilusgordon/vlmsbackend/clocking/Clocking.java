package com.theophilusgordon.vlmsbackend.clocking;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Clocking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UUID userId;
    private LocalDateTime clockInDateTime;
    private LocalDateTime clockOutDateTime;
    @Enumerated(EnumType.STRING)
    private WorkLocation workLocation;
}

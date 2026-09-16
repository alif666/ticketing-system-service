package com.pridesys.ticketing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 320)
    private String email;
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private UserRole role;
    @Column(nullable = false, length = 120)
    private String name;
    private String mobile, designation, office;
    @Column(nullable = false)
    private boolean active = true;
    @Column(name = "client_id")
    private Long clientId;

    public UserEntity(String email, String hash, UserRole role, String name, Long clientId) {
        this.email = email;
        passwordHash = hash;
        this.role = role;
        this.name = name;
        this.clientId = clientId;
    }

}

package com.pridesys.ticketing.entity;

import jakarta.persistence.*; import lombok.Getter; import lombok.Setter;

@Getter @Setter @Entity
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

    protected UserEntity() {
    }

    public UserEntity(String email, String hash, UserRole role, String name, Long clientId) {
        this.email = email;
        passwordHash = hash;
        this.role = role;
        this.name = name;
        this.clientId = clientId;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserRole getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getDesignation() {
        return designation;
    }

    public String getOffice() {
        return office;
    }

    public boolean isActive() {
        return active;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setPasswordHash(String v) {
        passwordHash = v;
    }

    public void setName(String v) {
        name = v;
    }

    public void setMobile(String v) {
        mobile = v;
    }

    public void setDesignation(String v) {
        designation = v;
    }

    public void setOffice(String v) {
        office = v;
    }

    public void setActive(boolean v) {
        active = v;
    }
}

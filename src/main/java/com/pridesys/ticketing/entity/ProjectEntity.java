package com.pridesys.ticketing.entity;

import jakarta.persistence.*; import lombok.Getter; import lombok.Setter;

@Getter @Setter @Entity
@Table(name = "projects")
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    @Column(name = "short_code", unique = true)
    String shortCode;
    String description;
    boolean active = true;

    protected ProjectEntity() {
    }

    public ProjectEntity(String n, String c, String d) {
        name = n;
        shortCode = c;
        description = d;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getShortCode() {
        return shortCode;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public void setName(String v) {
        name = v;
    }

    public void setDescription(String v) {
        description = v;
    }

    public void setActive(boolean v) {
        active = v;
    }
}

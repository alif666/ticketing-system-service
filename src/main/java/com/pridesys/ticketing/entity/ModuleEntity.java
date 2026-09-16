package com.pridesys.ticketing.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "modules", uniqueConstraints = @UniqueConstraint(columnNames = {"project_id", "name"}))
public class ModuleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "project_id")
    long projectId;
    String name, description;
    boolean active = true;

    protected ModuleEntity() {
    }

    public ModuleEntity(long p, String n, String d) {
        projectId = p;
        name = n;
        description = d;
    }

    public Long getId() {
        return id;
    }

    public long getProjectId() {
        return projectId;
    }

    public String getName() {
        return name;
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

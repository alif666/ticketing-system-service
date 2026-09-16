package com.pridesys.ticketing.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "project_memberships")
@IdClass(ProjectMembershipId.class)
public class ProjectMembershipEntity {
    @Id
    @Column(name = "project_id")
    long projectId;
    @Id
    @Column(name = "user_id")
    long userId;

    protected ProjectMembershipEntity() {
    }

    public ProjectMembershipEntity(long p, long u) {
        projectId = p;
        userId = u;
    }
}

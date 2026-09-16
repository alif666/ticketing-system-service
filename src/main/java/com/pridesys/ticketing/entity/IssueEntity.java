package com.pridesys.ticketing.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "issues")
public class IssueEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 240)
    private String title;
    @Lob
    @Column(nullable = false)
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private IssueType type;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private IssuePriority priority;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private IssueStage stage = IssueStage.SUBMITTED;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private VerificationStatus verificationStatus = VerificationStatus.NOT_REQUIRED;
    @Column(name = "project_id", nullable = false)
    private long projectId;
    @Column(name = "module_id")
    private Long moduleId;
    @Column(name = "reporter_id", nullable = false)
    private long reporterId;
    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;
    @Column(name = "updated_at", insertable = false, updatable = false)
    private Instant updatedAt;

    public IssueEntity(String t, String d, IssueType ty, IssuePriority p, long project, Long module, long reporter) {
        title = t;
        description = d;
        type = ty;
        priority = p;
        projectId = project;
        moduleId = module;
        reporterId = reporter;
    }
}

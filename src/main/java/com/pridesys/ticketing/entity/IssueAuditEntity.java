package com.pridesys.ticketing.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "issue_audits")
public class IssueAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "issue_id", nullable = false)
    private long issueId;
    @Column(name = "actor_id", nullable = false)
    private long actorId;
    @Column(nullable = false, length = 40)
    private String action;
    @Column(name = "field_name", length = 80)
    private String fieldName;
    @Lob
    private String oldValue, newValue;
    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    public IssueAuditEntity(long issue, long actor, String action, String field, String oldVal, String newVal) {
        issueId = issue;
        actorId = actor;
        this.action = action;
        fieldName = field;
        oldValue = oldVal;
        newValue = newVal;
    }
}

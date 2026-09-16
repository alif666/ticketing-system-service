package com.pridesys.ticketing.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "issue_comments")
public class IssueCommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "issue_id", nullable = false)
    private long issueId;
    @Column(name = "author_id", nullable = false)
    private long authorId;
    @Lob
    @Column(nullable = false)
    private String body;
    private boolean edited;
    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;
    @Column(name = "updated_at", insertable = false, updatable = false)
    private Instant updatedAt;

    public IssueCommentEntity(long issue, long author, String body) {
        issueId = issue;
        authorId = author;
        this.body = body;
    }
}

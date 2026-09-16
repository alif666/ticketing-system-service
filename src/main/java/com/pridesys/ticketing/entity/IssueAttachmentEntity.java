package com.pridesys.ticketing.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "issue_attachments")
public class IssueAttachmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "issue_id", nullable = false)
    private long issueId;
    @Column(name = "uploader_id", nullable = false)
    private long uploaderId;
    @Column(name = "original_name", nullable = false)
    private String originalName;
    @Column(name = "storage_key", nullable = false, unique = true)
    private String storageKey;
    @Column(name = "content_type", nullable = false)
    private String contentType;
    @Column(name = "size_bytes", nullable = false)
    private long sizeBytes;
    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    public IssueAttachmentEntity(long issue, long uploader, String name, String key, String type, long size) {
        issueId = issue;
        uploaderId = uploader;
        originalName = name;
        storageKey = key;
        contentType = type;
        sizeBytes = size;
    }
}

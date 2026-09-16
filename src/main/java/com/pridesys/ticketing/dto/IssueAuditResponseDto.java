package com.pridesys.ticketing.dto;

import java.time.Instant;

public record IssueAuditResponseDto(long id, long issueId, long actorId, String action, String fieldName,
                                    String oldValue, String newValue, Instant createdAt) {
}

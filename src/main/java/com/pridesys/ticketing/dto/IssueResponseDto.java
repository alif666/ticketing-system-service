package com.pridesys.ticketing.dto;
import com.pridesys.ticketing.entity.*; import java.time.Instant;
public record IssueResponseDto(long id,String title,String description,IssueType type,IssuePriority priority,IssueStage stage,VerificationStatus verificationStatus,long projectId,Long moduleId,long reporterId,Instant createdAt,Instant updatedAt) {}

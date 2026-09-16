package com.pridesys.ticketing.dto;
import com.pridesys.ticketing.entity.*; import jakarta.validation.constraints.*;
public record CreateIssueRequest(@NotBlank @Size(max=240) String title,@NotBlank String description,@NotNull IssueType type,@NotNull IssuePriority priority,@Positive long projectId,Long moduleId) {}

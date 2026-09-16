package com.pridesys.ticketing.dto;

import com.pridesys.ticketing.entity.*;
import jakarta.validation.constraints.*;

public record UpdateIssueRequest(@NotBlank @Size(max = 240) String title, @NotBlank String description,
                                 @NotNull IssueType type, @NotNull IssuePriority priority) {
}

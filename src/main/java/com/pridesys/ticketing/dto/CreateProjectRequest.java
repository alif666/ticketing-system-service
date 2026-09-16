package com.pridesys.ticketing.dto;
import jakarta.validation.constraints.*;
public record CreateProjectRequest(@NotBlank @Size(max=160) String name,@NotBlank @Size(max=30) String shortCode,@Size(max=1000) String description) {}

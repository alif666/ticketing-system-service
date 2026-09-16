package com.pridesys.ticketing.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public record UpdateProfileRequest(@NotBlank @Size(max=120) String name,@Size(max=30) String mobile,@Size(max=120) String designation,@Size(max=120) String office) {}

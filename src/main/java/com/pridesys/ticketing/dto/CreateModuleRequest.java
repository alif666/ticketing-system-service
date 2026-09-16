package com.pridesys.ticketing.dto;
import jakarta.validation.constraints.*;
public record CreateModuleRequest(@NotBlank @Size(max=160) String name,@Size(max=1000) String description) {}

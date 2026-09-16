package com.pridesys.ticketing.dto;
import jakarta.validation.constraints.*;
public record UpdateUserRequest(@NotBlank @Size(max=120) String name,@Size(max=30) String mobile,@Size(max=120) String designation,@Size(max=120) String office,Boolean active) {}

package com.pridesys.ticketing.dto;

import com.pridesys.ticketing.entity.UserRole;
import jakarta.validation.constraints.*;

public record CreateUserRequest(@Email @NotBlank String email, @NotBlank @Size(max = 120) String name,
                                @NotNull UserRole role, Long clientId) {
}

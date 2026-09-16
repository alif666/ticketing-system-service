package com.pridesys.ticketing.dto;

import jakarta.validation.constraints.*;

public record CreateClientRequest(@NotBlank @Size(max = 160) String name) {
}

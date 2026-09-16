package com.pridesys.ticketing.dto;

import jakarta.validation.constraints.*;

public record CommentRequest(@NotBlank @Size(max = 10000) String body) {
}

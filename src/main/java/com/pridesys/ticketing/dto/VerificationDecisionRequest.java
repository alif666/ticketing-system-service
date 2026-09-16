package com.pridesys.ticketing.dto;
import jakarta.validation.constraints.NotBlank;
public record VerificationDecisionRequest(@NotBlank String reason) {}

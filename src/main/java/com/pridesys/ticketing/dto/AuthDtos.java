package com.pridesys.ticketing.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class AuthDtos {
    private AuthDtos() {}

    public record LoginRequest(@Email @NotBlank String email, @NotBlank @Size(min = 8, max = 128) String password) {}
    public record LoginResponse(String token, ProfileResponse user) {}
    public record ForgotPasswordRequest(@Email @NotBlank String email) {}
    public record ResetPasswordRequest(@NotBlank String token, @NotBlank @Size(min = 8, max = 128) String newPassword) {}
    public record ChangePasswordRequest(@NotBlank String currentPassword, @NotBlank @Size(min = 8, max = 128) String newPassword) {}
    public record UpdateProfileRequest(@NotBlank @Size(max = 120) String name, @Size(max = 30) String mobile,
                                       @Size(max = 120) String designation, @Size(max = 120) String office) {}
    public record ProfileResponse(long id, String email, String role, String name, String mobile,
                                  String designation, String office, boolean active) {}
    public record MessageResponse(String message) {}
}

package com.pridesys.ticketing.auth.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.pridesys.ticketing.dto.*;
import com.pridesys.ticketing.auth.service.AuthService;
import com.pridesys.ticketing.security.util.JwtService;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final AuthService auth;

    public AuthController(AuthService auth) {
        this.auth = auth;
    }

    @PostMapping("/auth/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest req) {
        return auth.login(req.email(), req.password());
    }

    @PostMapping("/auth/forgot-password")
    public MessageResponse forgot(@Valid @RequestBody ForgotPasswordRequest req) {
        return new MessageResponse(auth.requestReset(req.email()));
    }

    @PostMapping("/auth/reset-password")
    public MessageResponse reset(@Valid @RequestBody ResetPasswordRequest req) {
        auth.resetPassword(req.token(), req.newPassword());
        return new MessageResponse("Password reset successfully");
    }

    @GetMapping("/me")
    public ProfileResponse me(Authentication authentication) {
        return auth.profile(principal(authentication).id());
    }

    @PatchMapping("/me")
    public ProfileResponse update(Authentication authentication, @Valid @RequestBody UpdateProfileRequest req) {
        return auth.updateProfile(principal(authentication).id(), req);
    }

    @PostMapping("/me/change-password")
    public MessageResponse change(Authentication authentication, @Valid @RequestBody ChangePasswordRequest req) {
        auth.changePassword(principal(authentication).id(), req.currentPassword(), req.newPassword());
        return new MessageResponse("Password changed successfully");
    }

    private JwtService.UserPrincipal principal(Authentication authentication) {
        return (JwtService.UserPrincipal) authentication.getPrincipal();
    }
}

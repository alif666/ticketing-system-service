package com.pridesys.ticketing.auth;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final AuthService auth;
    public AuthController(AuthService auth) { this.auth = auth; }

    @PostMapping("/auth/login") public AuthDtos.LoginResponse login(@Valid @RequestBody AuthDtos.LoginRequest req) { return auth.login(req.email(), req.password()); }
    @PostMapping("/auth/forgot-password") public AuthDtos.MessageResponse forgot(@Valid @RequestBody AuthDtos.ForgotPasswordRequest req) { return new AuthDtos.MessageResponse(auth.requestReset(req.email())); }
    @PostMapping("/auth/reset-password") public AuthDtos.MessageResponse reset(@Valid @RequestBody AuthDtos.ResetPasswordRequest req) { auth.resetPassword(req.token(), req.newPassword()); return new AuthDtos.MessageResponse("Password reset successfully"); }
    @GetMapping("/me") public AuthDtos.ProfileResponse me(Authentication authentication) { return auth.profile(principal(authentication).id()); }
    @PatchMapping("/me") public AuthDtos.ProfileResponse update(Authentication authentication, @Valid @RequestBody AuthDtos.UpdateProfileRequest req) { return auth.updateProfile(principal(authentication).id(), req); }
    @PostMapping("/me/change-password") public AuthDtos.MessageResponse change(Authentication authentication, @Valid @RequestBody AuthDtos.ChangePasswordRequest req) { auth.changePassword(principal(authentication).id(), req.currentPassword(), req.newPassword()); return new AuthDtos.MessageResponse("Password changed successfully"); }
    private JwtService.UserPrincipal principal(Authentication authentication) { return (JwtService.UserPrincipal) authentication.getPrincipal(); }
}

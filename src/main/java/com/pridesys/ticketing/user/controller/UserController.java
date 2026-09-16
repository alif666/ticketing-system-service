package com.pridesys.ticketing.user.controller;

import com.pridesys.ticketing.dto.*;
import com.pridesys.ticketing.entity.*;
import com.pridesys.ticketing.repository.UserRepository;
import com.pridesys.ticketing.user.service.IUserService;
import com.pridesys.ticketing.security.util.JwtService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository users;
    private final IUserService service;

    public UserController(UserRepository u, IUserService s) {
        users = u;
        service = s;
    }

    private UserEntity actor(Authentication a) {
        return users.findById(((JwtService.UserPrincipal) a.getPrincipal()).id()).orElseThrow();
    }

    @GetMapping
    public PageResponse<ProfileResponse> list(Authentication a, @RequestParam(defaultValue = "") String q, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return service.list(actor(a), q, page, size);
    }

    @PostMapping
    public ProfileResponse create(Authentication a, @Valid @RequestBody CreateUserRequest r) {
        return service.create(actor(a), r);
    }

    @PostMapping("/{id}/deactivate")
    public Map<String, String> deactivate(Authentication a, @PathVariable long id) {
        service.deactivate(actor(a), id);
        return Map.of("message", "User deactivated");
    }

    @PatchMapping("/{id}")
    public ProfileResponse update(Authentication a, @PathVariable long id, @Valid @RequestBody UpdateUserRequest r) {
        return service.update(actor(a), id, r);
    }
}

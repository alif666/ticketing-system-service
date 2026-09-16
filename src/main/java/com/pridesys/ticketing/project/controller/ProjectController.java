package com.pridesys.ticketing.project.controller;

import com.pridesys.ticketing.dto.*;
import com.pridesys.ticketing.entity.*;
import com.pridesys.ticketing.repository.UserRepository;
import com.pridesys.ticketing.project.service.IProjectService;
import com.pridesys.ticketing.security.util.JwtService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.*;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final UserRepository users;
    private final IProjectService service;

    public ProjectController(UserRepository u, IProjectService s) {
        users = u;
        service = s;
    }

    private UserEntity actor(Authentication a) {
        return users.findById(((JwtService.UserPrincipal) a.getPrincipal()).id()).orElseThrow();
    }

    @GetMapping
    public PageResponse<ProjectResponseDto> list(Authentication a, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return service.list(actor(a), page, size);
    }

    @PostMapping
    public ProjectResponseDto create(Authentication a, @Valid @RequestBody CreateProjectRequest r) {
        return service.create(actor(a), r);
    }

    @PatchMapping("/{id}")
    public Map<String, String> update(Authentication a, @PathVariable long id, @Valid @RequestBody UpdateProjectRequest r) {
        service.update(actor(a), id, r);
        return Map.of("message", "Project updated");
    }

    @PutMapping("/{projectId}/members/{userId}")
    public Map<String, String> add(Authentication a, @PathVariable long projectId, @PathVariable long userId) {
        service.member(actor(a), projectId, userId, true);
        return Map.of("message", "Member added");
    }

    @DeleteMapping("/{projectId}/members/{userId}")
    public Map<String, String> remove(Authentication a, @PathVariable long projectId, @PathVariable long userId) {
        service.member(actor(a), projectId, userId, false);
        return Map.of("message", "Member removed");
    }
}

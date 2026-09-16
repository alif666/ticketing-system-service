package com.pridesys.ticketing.project.service.impl;

import com.pridesys.ticketing.dto.*;
import com.pridesys.ticketing.entity.*;
import com.pridesys.ticketing.repository.*;
import com.pridesys.ticketing.project.service.IProjectService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl implements IProjectService {
    private final ProjectRepository projects;
    private final ProjectMembershipRepository memberships;

    public ProjectServiceImpl(ProjectRepository p, ProjectMembershipRepository m) {
        projects = p;
        memberships = m;
    }

    public PageResponse<ProjectResponseDto> list(UserEntity a, int page, int size) {
        var p = org.springframework.data.domain.PageRequest.of(Math.max(0, page), Math.min(Math.max(1, size), 100));
        var result = a.getRole() == UserRole.APP_ADMIN ? projects.findAll(p) : projects.forUser(a.getId(), p);
        return PageResponse.of(result.map(this::map));
    }

    public ProjectResponseDto create(UserEntity a, CreateProjectRequest r) {
        admin(a);
        return map(projects.save(new ProjectEntity(r.name().trim(), r.shortCode().trim().toUpperCase(), r.description())));
    }

    public void update(UserEntity a, long id, UpdateProjectRequest r) {
        admin(a);
        var p = projects.findById(id).orElseThrow();
        p.setName(r.name().trim());
        p.setDescription(r.description());
        if (r.active() != null) p.setActive(r.active());
        projects.save(p);
    }

    public void member(UserEntity a, long p, long u, boolean add) {
        admin(a);
        var id = new ProjectMembershipId(p, u);
        if (add) memberships.save(new ProjectMembershipEntity(p, u));
        else memberships.deleteById(id);
    }

    public boolean hasAccess(UserEntity a, long p) {
        return a.getRole() == UserRole.APP_ADMIN || projects.member(p, a.getId());
    }

    private void admin(UserEntity a) {
        if (a.getRole() != UserRole.APP_ADMIN) throw new AccessDeniedException("APP_ADMIN required");
    }

    private ProjectResponseDto map(ProjectEntity p) {
        return new ProjectResponseDto(p.getId(), p.getName(), p.getShortCode(), p.getDescription(), p.isActive());
    }
}

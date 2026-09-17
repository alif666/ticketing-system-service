package com.pridesys.ticketing.module.service.impl;

import com.pridesys.ticketing.dto.*;
import com.pridesys.ticketing.entity.*;
import com.pridesys.ticketing.repository.*;
import com.pridesys.ticketing.project.service.IProjectService;
import com.pridesys.ticketing.module.service.IModuleService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class ModuleServiceImpl implements IModuleService {
    private final ModuleRepository modules;
    private final IProjectService projects;

    public ModuleServiceImpl(ModuleRepository m, IProjectService p) {
        modules = m;
        projects = p;
    }

    public PageResponse<ModuleResponseDto> list(UserEntity a, long p, int page, int size) {
        scope(a, p);
        var result = modules.findByProjectIdOrderByName(p, org.springframework.data.domain.PageRequest.of(Math.max(0, page), Math.min(Math.max(1, size), 100)));
        return PageResponse.of(result.map(this::map));
    }

    public ModuleResponseDto create(UserEntity a, long p, CreateModuleRequest r) {
        scope(a, p);
        canManage(a);
        return map(modules.save(new ModuleEntity(p, r.name().trim(), r.description())));
    }

    public void update(UserEntity a, long p, long id, UpdateModuleRequest r) {
        scope(a, p);
        canManage(a);
        var m = modules.findById(id).orElseThrow();
        m.setName(r.name().trim());
        m.setDescription(r.description());
        if (r.active() != null) m.setActive(r.active());
        modules.save(m);
    }

    public void delete(UserEntity a, long p, long id) {
        scope(a, p);
        canManage(a);
        modules.deleteById(id);
    }

    private void canManage(UserEntity a) {
        if (a.getRole() != UserRole.APP_ADMIN && a.getRole() != UserRole.CLIENT_ADMIN)
            throw new AccessDeniedException("Module management denied");
    }

    private void scope(UserEntity a, long p) {
        if (!projects.hasAccess(a, p)) throw new AccessDeniedException("Project access denied");
    }

    private ModuleResponseDto map(ModuleEntity m) {
        return new ModuleResponseDto(m.getId(), m.getProjectId(), m.getName(), m.getDescription(), m.isActive());
    }
}

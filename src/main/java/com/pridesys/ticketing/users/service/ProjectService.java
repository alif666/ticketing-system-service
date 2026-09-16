package com.pridesys.ticketing.users.service;

import com.pridesys.ticketing.dto.*;
import com.pridesys.ticketing.entity.*;
import com.pridesys.ticketing.repository.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProjectService {
    private final ProjectRecordRepository projects;
    private final ModuleRecordRepository modules;
    private final UserRecordRepository users;

    public ProjectService(ProjectRecordRepository p, ModuleRecordRepository m, UserRecordRepository u) {
        projects = p;
        modules = m;
        users = u;
    }

    public List<ProjectRecord> projects(UserRecord a) {
        return a.role() == UserRole.APP_ADMIN ? projects.findAll() : projects.forUser(a.id());
    }

    public ProjectRecord create(UserRecord a, UsersProjectsDtos.CreateProjectRequest r) {
        admin(a);
        long id = projects.create(r);
        return projects.findAll().stream().filter(p -> p.id() == id).findFirst().orElseThrow();
    }

    public void update(UserRecord a, long id, UsersProjectsDtos.UpdateProjectRequest r) {
        admin(a);
        projects.update(id, r);
    }

    public void member(UserRecord a, long p, long u, boolean add) {
        admin(a);
        if (add) projects.addMember(p, u);
        else projects.removeMember(p, u);
    }

    public List<ModuleRecord> modules(UserRecord a, long p) {
        scope(a, p);
        return modules.findByProject(p);
    }

    public ModuleRecord createModule(UserRecord a, long p, UsersProjectsDtos.CreateModuleRequest r) {
        scope(a, p);
        long id = modules.create(p, r);
        return modules.findByProject(p).stream().filter(x -> x.id() == id).findFirst().orElseThrow();
    }

    public void updateModule(UserRecord a, long p, long id, UsersProjectsDtos.UpdateModuleRequest r) {
        scope(a, p);
        modules.update(id, r);
    }

    public void deleteModule(UserRecord a, long p, long id) {
        scope(a, p);
        modules.delete(id);
    }

    private void scope(UserRecord a, long p) {
        if (a.role() == UserRole.APP_ADMIN) return;
        if (!projects.member(p, a.id())) throw new AccessDeniedException("Project access denied");
    }

    private void admin(UserRecord a) {
        if (a.role() != UserRole.APP_ADMIN) throw new AccessDeniedException("APP_ADMIN required");
    }
}

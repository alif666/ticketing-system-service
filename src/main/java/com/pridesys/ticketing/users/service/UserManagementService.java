package com.pridesys.ticketing.users.service;

import com.pridesys.ticketing.dto.*;
import com.pridesys.ticketing.entity.*;
import com.pridesys.ticketing.repository.UserRecordRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserManagementService {
    private final UserRecordRepository users;
    private final PasswordEncoder encoder;

    public UserManagementService(UserRecordRepository u, PasswordEncoder e) {
        users = u;
        encoder = e;
    }

    public UserManagementService(UserRecordRepository u) {
        this(u, null);
    }

    public List<AuthDtos.ProfileResponse> list(UserRecord actor, String search, int page, int size) {
        requireAdmin(actor);
        int offset = Math.max(0, page) * Math.min(Math.max(1, size), 100);
        return users.search(search, offset, Math.min(Math.max(1, size), 100)).stream().filter(u -> visible(actor, u.id())).map(this::profile).toList();
    }

    public AuthDtos.ProfileResponse create(UserRecord actor, CreateUser req) {
        requireAdmin(actor);
        if (actor.role() == UserRole.CLIENT_ADMIN && req.role() == UserRole.APP_ADMIN)
            throw new AccessDeniedException("Insufficient role");
        Long client = req.clientId();
        if (actor.role() != UserRole.APP_ADMIN) {
            client = users.clientId(actor.id()).orElseThrow();
            if (req.clientId() != null && !req.clientId().equals(client))
                throw new AccessDeniedException("Outside client scope");
        }
        var r = new UsersProjectsDtos.CreateUserRequest(req.email(), req.name(), req.role(), client);
        long id = users.create(r, encoder == null ? "" : encoder.encode("Password123!"));
        return profile(users.findById(id).orElseThrow());
    }

    public void deactivate(UserRecord actor, long id) {
        requireAdmin(actor);
        target(actor, id);
        users.update(id, new UsersProjectsDtos.UpdateUserRequest(users.findById(id).orElseThrow().name(), null, null, null, false));
    }

    public AuthDtos.ProfileResponse update(UserRecord actor, long id, UsersProjectsDtos.UpdateUserRequest req) {
        requireAdmin(actor);
        target(actor, id);
        users.update(id, req);
        return profile(users.findById(id).orElseThrow());
    }

    private boolean visible(UserRecord a, long id) {
        return a.role() == UserRole.APP_ADMIN || users.clientId(a.id()).equals(users.clientId(id));
    }

    private UserRecord target(UserRecord a, long id) {
        var u = users.findById(id).orElseThrow();
        if (!visible(a, id)) throw new AccessDeniedException("Outside client scope");
        if (a.role() == UserRole.CLIENT_ADMIN && u.role() == UserRole.APP_ADMIN)
            throw new AccessDeniedException("Insufficient role");
        return u;
    }

    private void requireAdmin(UserRecord a) {
        if (a.role() == UserRole.CLIENT_USER) throw new AccessDeniedException("Insufficient role");
    }

    private AuthDtos.ProfileResponse profile(UserRecord u) {
        return new AuthDtos.ProfileResponse(u.id(), u.email(), u.role().name(), u.name(), u.mobile(), u.designation(), u.office(), u.active());
    }

    public record CreateUser(String email, String name, UserRole role, Long clientId) {
    }
}

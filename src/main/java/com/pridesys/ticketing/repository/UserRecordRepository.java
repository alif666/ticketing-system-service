package com.pridesys.ticketing.repository;

import com.pridesys.ticketing.dto.UsersProjectsDtos;
import com.pridesys.ticketing.entity.*;

import java.util.*;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

@Repository
public class UserRecordRepository {
    private final UserRepository repo;

    public UserRecordRepository(UserRepository r) {
        repo = r;
    }

    public Optional<UserRecord> findByEmail(String e) {
        return repo.findByEmailIgnoreCase(e.trim()).map(this::record);
    }

    public Optional<UserRecord> findById(long id) {
        return repo.findById(id).map(this::record);
    }

    public Optional<Long> clientId(long id) {
        return repo.clientId(id);
    }

    public List<UserRecord> search(String q, int offset, int limit) {
        return repo.search(q, PageRequest.of(offset / limit, limit)).getContent().stream().map(this::record).toList();
    }

    public long count(String q) {
        return repo.search(q, PageRequest.of(0, 1)).getTotalElements();
    }

    public long create(UsersProjectsDtos.CreateUserRequest r, String hash) {
        var e = new UserEntity(r.email().trim().toLowerCase(), hash, r.role(), r.name().trim(), r.clientId());
        return repo.save(e).getId();
    }

    public void update(long id, UsersProjectsDtos.UpdateUserRequest r) {
        var e = repo.findById(id).orElseThrow();
        e.setName(r.name().trim());
        e.setMobile(r.mobile());
        e.setDesignation(r.designation());
        e.setOffice(r.office());
        if (r.active() != null) e.setActive(r.active());
        repo.save(e);
    }

    public void updateProfile(long id, String n, String m, String d, String o) {
        var e = repo.findById(id).orElseThrow();
        e.setName(n);
        e.setMobile(m);
        e.setDesignation(d);
        e.setOffice(o);
        repo.save(e);
    }

    public void updatePassword(long id, String h) {
        var e = repo.findById(id).orElseThrow();
        e.setPasswordHash(h);
        repo.save(e);
    }

    private UserRecord record(UserEntity e) {
        return new UserRecord(e.getId(), e.getEmail(), e.getPasswordHash(), e.getRole(), e.getName(), e.getMobile(), e.getDesignation(), e.getOffice(), e.isActive());
    }
}

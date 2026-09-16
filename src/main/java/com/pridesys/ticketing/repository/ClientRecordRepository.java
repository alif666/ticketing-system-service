package com.pridesys.ticketing.repository;

import com.pridesys.ticketing.dto.*;
import com.pridesys.ticketing.entity.*;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class ClientRecordRepository {
    private final ClientRepository repo;

    public ClientRecordRepository(ClientRepository r) {
        repo = r;
    }

    public List<ClientRecord> findAll() {
        return repo.findAll().stream().map(e -> new ClientRecord(e.getId(), e.getName(), e.isActive())).toList();
    }

    public long create(UsersProjectsDtos.CreateClientRequest r) {
        return repo.save(new ClientEntity(r.name().trim())).getId();
    }
}

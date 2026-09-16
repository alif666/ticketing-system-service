package com.pridesys.ticketing.client.service.impl;

import com.pridesys.ticketing.client.service.IClientService;
import com.pridesys.ticketing.dto.*;
import com.pridesys.ticketing.entity.*;
import com.pridesys.ticketing.repository.ClientRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements IClientService {
    private final ClientRepository clients;

    public ClientServiceImpl(ClientRepository c) {
        clients = c;
    }

    public PageResponse<ClientResponseDto> list(UserEntity a, int page, int size) {
        admin(a);
        var p = clients.findAll(org.springframework.data.domain.PageRequest.of(Math.max(0, page), Math.min(Math.max(1, size), 100)));
        return PageResponse.of(p.map(c -> new ClientResponseDto(c.getId(), c.getName(), c.isActive())));
    }

    public ClientResponseDto create(UserEntity a, CreateClientRequest r) {
        admin(a);
        var c = clients.save(new ClientEntity(r.name().trim()));
        return new ClientResponseDto(c.getId(), c.getName(), c.isActive());
    }

    private void admin(UserEntity a) {
        if (a.getRole() != UserRole.APP_ADMIN) throw new AccessDeniedException("APP_ADMIN required");
    }
}

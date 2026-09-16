package com.pridesys.ticketing.client.controller;

import com.pridesys.ticketing.client.service.IClientService;
import com.pridesys.ticketing.dto.*;
import com.pridesys.ticketing.entity.UserEntity;
import com.pridesys.ticketing.repository.UserRepository;
import com.pridesys.ticketing.security.util.JwtService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.*;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    private final UserRepository users;
    private final IClientService service;

    public ClientController(UserRepository u, IClientService s) {
        users = u;
        service = s;
    }

    private UserEntity actor(Authentication a) {
        return users.findById(((JwtService.UserPrincipal) a.getPrincipal()).id()).orElseThrow();
    }

    @GetMapping
    public PageResponse<ClientResponseDto> list(Authentication a, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return service.list(actor(a), page, size);
    }

    @PostMapping
    public ClientResponseDto create(Authentication a, @Valid @RequestBody CreateClientRequest r) {
        return service.create(actor(a), r);
    }
}

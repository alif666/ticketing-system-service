package com.pridesys.ticketing.client.service;

import com.pridesys.ticketing.dto.*;
import com.pridesys.ticketing.entity.UserEntity;

public interface IClientService {
    PageResponse<ClientResponseDto> list(UserEntity actor, int page, int size);

    ClientResponseDto create(UserEntity actor, CreateClientRequest request);
}

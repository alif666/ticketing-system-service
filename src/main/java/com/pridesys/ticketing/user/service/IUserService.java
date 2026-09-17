package com.pridesys.ticketing.user.service;

import com.pridesys.ticketing.dto.*;
import com.pridesys.ticketing.entity.UserEntity;

public interface IUserService {
    PageResponse<ProfileResponse> list(UserEntity actor, String q, int page, int size);

    ProfileResponse create(UserEntity actor, CreateUserRequest request);

    void deactivate(UserEntity actor, long id);

    void delete(UserEntity actor, long id);

    ProfileResponse update(UserEntity actor, long id, UpdateUserRequest request);
}

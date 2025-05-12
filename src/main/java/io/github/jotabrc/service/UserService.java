package io.github.jotabrc.service;

import io.github.jotabrc.dto.UserDto;

public interface UserService {

    String add(final UserDto dto);
    void update(final UserDto dto) throws Exception;
    UserDto findByUuid(final String uuid);
}

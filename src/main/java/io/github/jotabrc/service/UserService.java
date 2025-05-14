package io.github.jotabrc.service;

import io.github.jotabrc.dto.UserAuthDto;
import io.github.jotabrc.dto.UserDto;

public interface UserService {

    String add(final UserDto dto);
    void update(final UserDto dto) throws Exception;
    void auth(final UserAuthDto dto) throws Exception;
    UserDto findByUuid();
}

package io.github.jotabrc.service;

import io.github.jotabrc.dto.UserAuthDto;
import io.github.jotabrc.dto.UserDto;
import io.github.jotabrc.dto.UserRegisterDto;

public interface UserService {

    String add(final UserRegisterDto dto);
    void update(final UserRegisterDto dto) throws Exception;
    void auth(final UserAuthDto dto) throws Exception;
    UserDto findByUuid();
}

package io.github.jotabrc.service;

import io.github.jotabrc.config.DependencyInjection;
import io.github.jotabrc.dto.AddUser;
import io.github.jotabrc.repository.RoleRepository;
import io.github.jotabrc.repository.UserRepository;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl() {
        this.userRepository = DependencyInjection.createUserRepository();
        this.roleRepository = DependencyInjection.createRoleRepository();
    }

    @Override
    public String add(AddUser user) {


        return "";
    }
}

package io.github.jotabrc.config;

import io.github.jotabrc.repository.RoleRepository;
import io.github.jotabrc.repository.RoleRepositoryImpl;
import io.github.jotabrc.util.*;

public class DependencyInjection {

    public static PropertiesLoader createPropertiesLoader() {
        return new PropertiesLoaderImpl();
    }

    public static LoadEnvironmentVariables createLoadEnvironmentVariables() {
        return new LoadEnvironmentVariablesImpl();
    }

    public static ConnectionUtil createConnectionUtil() {
        return new ConnectionUtilImpl();
    }

    public static RoleRepository createRoleRepository() {
        return new RoleRepositoryImpl();
    }
}

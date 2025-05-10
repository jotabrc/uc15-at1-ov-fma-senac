package io.github.jotabrc.config;

import io.github.jotabrc.repository.RoleRepository;
import io.github.jotabrc.repository.RoleRepositoryImpl;
import io.github.jotabrc.repository.UserRepository;
import io.github.jotabrc.repository.UserRepositoryImpl;
import io.github.jotabrc.repository.util.PrepareStatement;
import io.github.jotabrc.repository.util.PrepareStatementImpl;
import io.github.jotabrc.repository.util.SqlBuilder;
import io.github.jotabrc.repository.util.SqlBuilderImpl;
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

    public static UserRepository createUserRepository() {
        return new UserRepositoryImpl();
    }

    public static SqlBuilder createSqlBuilder() {
        return new SqlBuilderImpl();
    }

    public static PrepareStatement createPrepareStatement() {
        return new PrepareStatementImpl();
    }
}

package io.github.jotabrc;

import io.github.jotabrc.config.DatabaseConfig;
import io.github.jotabrc.config.DependencyInjection;
import io.github.jotabrc.dto.RoleDto;
import io.github.jotabrc.dto.UserAuthDto;
import io.github.jotabrc.dto.UserDto;
import io.github.jotabrc.security.ApplicationContext;
import io.github.jotabrc.service.RoleService;
import io.github.jotabrc.service.RoleServiceImpl;
import io.github.jotabrc.service.UserService;
import io.github.jotabrc.service.UserServiceImpl;
import io.github.jotabrc.util.LoadEnvironmentVariables;
import io.github.jotabrc.util.RoleName;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;

@Slf4j
public class Main {

    private static final DatabaseConfig databaseConfig = DatabaseConfig.getInstance();
    private static final LoadEnvironmentVariables loadEnv = DependencyInjection.createLoadEnvironmentVariables();

    public static void main(String[] args) {
        try {
            loadEnv.loadDatabaseVariables();
            migrateDb();
            addRole();
            addUser();
            UserService userService = new UserServiceImpl(
                    DependencyInjection.createUserRepository(),
                    DependencyInjection.createRoleRepository(),
                    ApplicationContext.getInstance());
            userService.auth(
                    UserAuthDto
                            .builder()
                            .email("email@email")
                            .password("password1234")
                            .build()
            );
            System.out.println(ApplicationContext.getInstance().getUserUuid());
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    private static void migrateDb() {
        try {
            var flyway = Flyway.configure()
                    .locations("classpath:db/migration")
                    .dataSource("jdbc:" +
                                    databaseConfig.getDB_TYPE() + "://" +
                                    databaseConfig.getDB_HOST() + ":" +
                                    databaseConfig.getDB_PORT() + "/" +
                                    databaseConfig.getDB_SCHEMA(),
                            databaseConfig.getDB_USER(), databaseConfig.getDB_PASSWORD())
                    .load();
            flyway.migrate();
        } catch (FlywayException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * AT 1 requirement.
     */
    private static void addRole() {
        RoleService roleService = new RoleServiceImpl(DependencyInjection.createRoleRepository());
        try {
            roleService.add(
                    RoleDto
                            .builder()
                            .name(RoleName.USER)
                            .description("User")
                            .build()
            );
            roleService.add(
                    RoleDto
                            .builder()
                            .name(RoleName.ADMIN)
                            .description("Admin")
                            .build()
            );
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    /**
     * AT 1 requirement.
     */
    private static void addUser() {
        UserService userService = new UserServiceImpl(
                DependencyInjection.createUserRepository(),
                DependencyInjection.createRoleRepository(),
                ApplicationContext.getInstance()
        );
        try {
            userService.add(
                    UserDto
                            .builder()
                            .username("example")
                            .email("email@email")
                            .name("John Doe")
                            .password("password1234")
                            .build()
            );
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }
}

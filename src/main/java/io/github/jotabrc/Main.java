package io.github.jotabrc;

import io.github.jotabrc.config.DatabaseConfig;
import io.github.jotabrc.config.DependencyInjection;
import io.github.jotabrc.dto.AddRole;
import io.github.jotabrc.service.RoleService;
import io.github.jotabrc.service.RoleServiceImpl;
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
        loadEnv.loadDatabaseVariables();
        migrateDb();
        addRole();
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
            log.warn(e.getMessage());
        }
    }

    private static void addRole() {
        RoleService roleService = new RoleServiceImpl();
        try {
            roleService.add(
                    AddRole
                            .builder()
                            .name(RoleName.USER)
                            .description("User")
                            .build()
            );
            roleService.add(
                    AddRole
                            .builder()
                            .name(RoleName.ADMIN)
                            .description("Admin")
                            .build()
            );
        } catch (Exception e) {
           log.info(e.getMessage());
        }
    }
}

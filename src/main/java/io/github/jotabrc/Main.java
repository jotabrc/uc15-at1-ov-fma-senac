package io.github.jotabrc;

import io.github.jotabrc.config.DatabaseConfig;
import io.github.jotabrc.config.DependencyInjection;
import io.github.jotabrc.dto.AddRole;
import io.github.jotabrc.service.RoleService;
import io.github.jotabrc.service.RoleServiceImpl;
import io.github.jotabrc.util.LoadEnvironmentVariables;
import io.github.jotabrc.util.RoleName;
import org.flywaydb.core.Flyway;

public class Main {

    private static final DatabaseConfig databaseConfig = DatabaseConfig.getInstance();
    private static final LoadEnvironmentVariables loadEnv = DependencyInjection.createLoadEnvironmentVariables();

    public static void main(String[] args) {
        loadEnv.loadDatabaseVariables();
        migrateDb();

        RoleService roleService = new RoleServiceImpl();
        roleService.add(
                AddRole
                        .builder()
                        .name(RoleName.USER)
                        .description("User")
                        .build()
        );
    }

    private static void migrateDb() {
        var flyway = Flyway.configure()
                .dataSource("jdbc:" +
                                databaseConfig.getDB_TYPE() + "://" +
                                databaseConfig.getDB_HOST() + ":" +
                                databaseConfig.getDB_PORT() + "/" +
                                databaseConfig.getDB_SCHEMA(),
                        databaseConfig.getDB_USER(), databaseConfig.getDB_PASSWORD())
                .load();
        flyway.migrate();
    }
}

package io.github.jotabrc;

import io.github.jotabrc.config.DatabaseConfig;
import io.github.jotabrc.config.DependencyInjection;
import io.github.jotabrc.util.LoadEnvironmentVariables;
import org.flywaydb.core.Flyway;

public class Main {

    private static final DatabaseConfig databaseConfig = DatabaseConfig.getInstance();
    private static final LoadEnvironmentVariables loadEnv = DependencyInjection.createLoadEnvironmentVariables();

    public static void main(String[] args) {
        loadEnv.load();
        migrateDb();
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

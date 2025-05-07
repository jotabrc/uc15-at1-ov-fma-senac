package io.github.jotabrc;

import io.github.jotabrc.config.DatabaseConfig;
import io.github.jotabrc.util.LoadEnvironmentVariablesImpl;
import org.flywaydb.core.Flyway;

public class Main {

    private static final DatabaseConfig databaseConfig = DatabaseConfig.getInstance();

    public static void main(String[] args) {
        LoadEnvironmentVariablesImpl load = new LoadEnvironmentVariablesImpl();
        load.load();
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

package io.github.jotabrc.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Setter
@Getter
public class DatabaseConfig {

    private String DB_TYPE;
    private String DB_USER;
    private String DB_PASSWORD;
    private String DB_HOST;
    private String DB_PORT;
    private String DB_SCHEMA;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static DatabaseConfig databaseConfig;

    private DatabaseConfig() {}

    public static DatabaseConfig getInstance() {
        if (databaseConfig == null) databaseConfig = new DatabaseConfig();
        return databaseConfig;
    }
}

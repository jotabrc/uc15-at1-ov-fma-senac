package io.github.jotabrc.util;

import io.github.jotabrc.config.DatabaseConfig;
import io.github.jotabrc.config.DependencyInjection;

import java.util.Properties;

public class LoadEnvironmentVariablesImpl implements LoadEnvironmentVariables {

    private final DatabaseConfig databaseConfig;

    public LoadEnvironmentVariablesImpl() {
        this.databaseConfig = DatabaseConfig.getInstance();
    }

    @Override
    public void loadDatabaseVariables() {
        Properties properties = DependencyInjection.createPropertiesLoader().load("database-application.properties");
        properties.forEach((k, v) -> {
            String value = v.toString();
            String envVar = extractVar(value);
            String env = System.getenv(envVar);

            switch (envVar) {
                case "DB_TYPE" -> databaseConfig.setDB_TYPE(ifNullDefault(env, "mysql"));
                case "DB_USER" -> databaseConfig.setDB_USER(ifNullDefault(env, "root"));
                case "DB_PASSWORD" -> databaseConfig.setDB_PASSWORD(ifNullDefault(env, "root"));
                case "DB_HOST" -> databaseConfig.setDB_HOST(ifNullDefault(env, "localhost"));
                case "DB_PORT" -> databaseConfig.setDB_PORT(Integer.parseInt(ifNullDefault(env, "3336")));
                case "DB_SCHEMA" -> databaseConfig.setDB_SCHEMA(ifNullDefault(env, "defaultdb"));
            }
        });
    }

    private String extractVar(final String value) {
        return (value.startsWith("${") && value.endsWith("}") ? value.substring(2, value.length() - 1) : value);
    }

    private String ifNullDefault(String value, String defaultValue) {
        return (value == null || value.isBlank()) ? defaultValue : value;
    }
}

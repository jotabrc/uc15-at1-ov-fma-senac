package io.github.jotabrc.util;

import com.mysql.cj.jdbc.MysqlDataSource;
import io.github.jotabrc.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionUtilImpl implements ConnectionUtil {

    private final DatabaseConfig databaseConfig = DatabaseConfig.getInstance();

    @Override
    public Connection getCon() throws SQLException {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName(databaseConfig.getDB_HOST());
        dataSource.setPortNumber(databaseConfig.getDB_PORT());
        dataSource.setDatabaseName(databaseConfig.getDB_SCHEMA());
        dataSource.setUser(databaseConfig.getDB_USER());
        dataSource.setPassword(databaseConfig.getDB_PASSWORD());

        return  dataSource.getConnection();
    }
}

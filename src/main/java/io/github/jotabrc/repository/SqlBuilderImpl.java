package io.github.jotabrc.repository;

import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.Map;

public class SqlBuilderImpl implements SqlBuilder {

    @Override
    public PreparedStatement build(String dqml, String table, Map<String, Object> columnAndValue, Map<String, Object> columnAndValueCondition, String... fields) {
        /*
         * dqml = INSERT
         * table = tb_user
         * conditions = USERNAME, NAME
         */
        StringBuilder sql = new StringBuilder();

        switch (dqml) {
            case "INSERT" -> sql
                    .append("INSERT INTO")
                    .append(" ")
                    .append(table);
            case "UPDATE" -> {
                sql
                        .append("UPDATE")
                        .append(" ")
                        .append(table)
                        .append("SET");

                for (var entry : columnAndValue.entrySet()) {
                    if (sql.charAt(sql.length() - 1) == '?') sql
                            .append(" ")
                            .append("AND");
                    sql
                            .append(" ")
                            .append(entry.getKey())
                            .append("=")
                            .append("?");
                }
            }
            case "SELECT" -> {
                sql.append("SELECT");
            }
        }


        return "";
    }
}

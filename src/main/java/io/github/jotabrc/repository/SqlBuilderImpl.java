package io.github.jotabrc.repository;

import java.sql.PreparedStatement;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Stream;

public class SqlBuilderImpl implements SqlBuilder {

    @Override
    public PreparedStatement build(String dqml, String table, Map<String, Object> columnsAndValues, Map<String, Object> conditions, String... fields) {

        StringBuilder sql = new StringBuilder();

        switch (dqml) {
            case "INSERT" -> sql
                    .append("INSERT INTO ")
                    .append(table)
                    .append(" ")
                    .append(join(",", "(", ")", columnsAndValues))
                    .append(" VALUES ")
                    .append(join(",", "?", columnsAndValues.size()));
            case "UPDATE" -> {
                sql
                        .append("UPDATE ")
                        .append(table)
                        .append(" SET ")
                        .append(
                                join(", ",
                                        columnsAndValues
                                                .keySet()
                                                .stream()
                                                .map(o -> o + " = ?")
                                                .toArray(String[]::new)
                                )
                        );
                buildConditions(conditions, sql);
            }
            case "SELECT" -> {
                sql
                        .append("SELECT ")
                        .append(join(",c", fields))
                        .append(" FROM ")
                        .append(table);
                buildConditions(conditions, sql);
            }
        }
        return "";
    }

    private void buildConditions(Map<String, Object> conditions, StringBuilder sql) {
        if (conditions != null && !conditions.isEmpty())
            sql
                    .append(" WHERE ")
                    .append(
                            join("AND ",
                                    conditions
                                            .keySet()
                                            .stream()
                                            .map(o -> o + " = ?")
                                            .toArray(String[]::new)
                            )
                    );
    }

    private String join(String delimiter, Map<String, Object> map) {
        StringJoiner joiner = new StringJoiner(delimiter);
        for (var entry : map.entrySet()) {
            joiner.add(entry.getKey());
        }
        return joiner.toString();
    }

    private String join(String delimiter, String prefix, String suffix, Map<String, Object> map) {
        StringJoiner joiner = new StringJoiner(delimiter, prefix, suffix);
        for (var entry : map.entrySet()) {
            joiner.add(entry.getKey());
        }
        return joiner.toString();
    }

    private String join(String delimiter, String... fields) {
        StringJoiner joiner = new StringJoiner(delimiter);
        for (var field : fields) {
            joiner.add(field);
        }
        return joiner.toString();
    }

    private String join(String delimiter, String setter, int quantity) {
        StringJoiner joiner = new StringJoiner(delimiter);
        Stream.generate(() -> setter)
                .limit(quantity)
                .forEach(joiner::add);
        return joiner.toString();
    }

}

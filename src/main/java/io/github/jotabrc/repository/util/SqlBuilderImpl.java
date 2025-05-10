package io.github.jotabrc.repository.util;

import java.util.LinkedHashMap;
import java.util.StringJoiner;
import java.util.stream.Stream;

public class SqlBuilderImpl implements SqlBuilder {

    @Override
    public String build(String dqml, String table, LinkedHashMap<String, Object> columnsAndValues, LinkedHashMap<String, Object> conditions, String[] fields) {

        StringBuilder sql = new StringBuilder();

        switch (dqml) {
            case "INSERT" -> sql
                    .append("INSERT INTO ")
                    .append(table)
                    .append(" ")
                    .append(join(",", "(", ")", columnsAndValues))
                    .append(" VALUES ")
                    .append(join(",", "?", "(", ")", columnsAndValues.size()));
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
                        .append(join(",", fields))
                        .append(" FROM ")
                        .append(table);
                buildConditions(conditions, sql);
            }
        }
        return sql.toString();
    }

    @Override
    public String build(String dqml, String table, LinkedHashMap<String, Object> columnsAndValues, LinkedHashMap<String, Object> conditions) {
        return build(dqml, table, columnsAndValues, conditions, null);
    }

    @Override
    public String build(String dqml, String table, LinkedHashMap<String, Object> conditions, String[] fields) {
        return build(dqml, table, null, conditions, fields);
    }

    @Override
    public String build(String dqml, String table, LinkedHashMap<String, Object> columnsAndValues) {
        return build(dqml, table, columnsAndValues, null, null);
    }

    private void buildConditions(LinkedHashMap<String, Object> conditions, StringBuilder sql) {
        if (conditions != null && !conditions.isEmpty())
            sql
                    .append(" WHERE ")
                    .append(
                            join(" AND ",
                                    conditions
                                            .keySet()
                                            .stream()
                                            .map(o -> o + " = ?")
                                            .toArray(String[]::new)
                            )
                    );
    }

    private String join(String delimiter, LinkedHashMap<String, Object> map) {
        StringJoiner joiner = new StringJoiner(delimiter);
        for (var entry : map.entrySet()) {
            joiner.add(entry.getKey());
        }
        return joiner.toString();
    }

    private String join(String delimiter, String prefix, String suffix, LinkedHashMap<String, Object> map) {
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

    private String join(String delimiter, String setter, String prefix, String suffix, int quantity) {
        StringJoiner joiner = new StringJoiner(delimiter, prefix, suffix);
        Stream.generate(() -> setter)
                .limit(quantity)
                .forEach(joiner::add);
        return joiner.toString();
    }

}

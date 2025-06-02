package io.github.jotabrc.util;

import io.github.jotabrc.dto.PageFilter;
import io.github.jotabrc.repository.util.DQML;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Stream;

public class DatabaseStatement {

    private DatabaseStatement() {}

    public record Data(String table, DQML dqml, LinkedHashMap<String, Object> columnsAndValues, LinkedHashMap<String, Object> conditions, List<String> fields, PageFilter pageFilter) {}

    public static String buildStatement(Data data) {
        StringBuilder sql = new StringBuilder();

        switch (data.dqml()) {
            case INSERT -> sql
                    .append("INSERT INTO ")
                    .append(data.table)
                    .append(" ")
                    .append(join(",", "(", ")", data.columnsAndValues))
                    .append(" VALUES ")
                    .append(join(",", "?", "(", ")", data.columnsAndValues.size()));
            case UPDATE -> {
                sql
                        .append("UPDATE ")
                        .append(data.table)
                        .append(" SET ")
                        .append(
                                join(", ",
                                        data.columnsAndValues
                                                .keySet()
                                                .stream()
                                                .map(o -> o + " = ?")
                                                .toList()
                                )
                        );
                buildConditions(data.conditions, sql);
            }
            case SELECT -> {
                sql
                        .append("SELECT ")
                        .append(join(",", data.fields))
                        .append(" FROM ")
                        .append(data.table);
                buildConditions(data.conditions, sql);
            }
        }
        return sql.toString();
    }

    private static void buildConditions(LinkedHashMap<String, Object> conditions, StringBuilder sql) {
        if (conditions != null && !conditions.isEmpty())
            sql
                    .append(" WHERE ")
                    .append(
                            join(" AND ",
                                    conditions
                                            .keySet()
                                            .stream()
                                            .map(o -> o + " = ?")
                                            .toList()
                            )
                    );
    }

    private static String join(String delimiter, String prefix, String suffix, LinkedHashMap<String, Object> map) {
        StringJoiner joiner = new StringJoiner(delimiter, prefix, suffix);
        for (var entry : map.entrySet()) {
            joiner.add(entry.getKey());
        }
        return joiner.toString();
    }

    private static String join(String delimiter, List<String> fields) {
        StringJoiner joiner = new StringJoiner(delimiter);
        for (var field : fields) {
            joiner.add(field);
        }
        return joiner.toString();
    }

    private static String join(String delimiter, String setter, String prefix, String suffix, int quantity) {
        StringJoiner joiner = new StringJoiner(delimiter, prefix, suffix);
        Stream.generate(() -> setter)
                .limit(quantity)
                .forEach(joiner::add);
        return joiner.toString();
    }
}

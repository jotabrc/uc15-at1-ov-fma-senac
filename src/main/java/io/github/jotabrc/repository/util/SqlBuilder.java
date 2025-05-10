package io.github.jotabrc.repository.util;

import java.util.LinkedHashMap;

public interface SqlBuilder {

    String build(final String dqml, final String table, final LinkedHashMap<String, Object> columnsAndValues, final LinkedHashMap<String, Object> conditions, final String[] fields);
    String build(final String dqml, final String table, final LinkedHashMap<String, Object> columnsAndValues, final LinkedHashMap<String, Object> conditions);
    String build(final String dqml, final String table, final LinkedHashMap<String, Object> conditions, final String[] fields);
    String build(final String dqml, final String table, final LinkedHashMap<String, Object> columnsAndValues);
}

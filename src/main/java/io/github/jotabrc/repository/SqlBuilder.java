package io.github.jotabrc.repository;

import java.util.Map;

public interface SqlBuilder {

    String build(final String dqml, final String table, final Map<String, Object> columnAndValue, final Map<String, Object> columnAndValueCondition, String... fields);
}

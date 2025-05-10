package io.github.jotabrc.repository.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public interface PrepareStatement {

    PreparedStatement prepare(final PreparedStatement ps, final LinkedHashMap<String, Object> values) throws SQLException;
}

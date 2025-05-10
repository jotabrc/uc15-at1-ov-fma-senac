package io.github.jotabrc.repository.util;

import io.github.jotabrc.util.RoleName;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;

public class PrepareStatementImpl implements PrepareStatement {

    @Override
    public PreparedStatement prepare(PreparedStatement ps, LinkedHashMap<String, Object> values) throws SQLException {
        int index = 1;
        for (var value : values.values()) {
            if (value instanceof String v) ps.setString(index, v);
            else if (value instanceof Long v) ps.setLong(index, v);
            else if (value instanceof Timestamp v) ps.setTimestamp(index, v);
            else if (value instanceof Boolean v) ps.setBoolean(index, v);
            else if (value instanceof Integer v) ps.setInt(index, v);
            else if (value instanceof LocalDateTime v) ps.setTimestamp(index, Timestamp.from(Instant.from(v)));
            else if (value instanceof RoleName v) ps.setString(index, v.getName());
            else ps.setObject(index, value);
            index++;
        }

        return ps;
    }
}

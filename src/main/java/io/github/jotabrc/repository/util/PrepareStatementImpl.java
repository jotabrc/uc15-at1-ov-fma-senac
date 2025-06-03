package io.github.jotabrc.repository.util;

import io.github.jotabrc.util.RoleName;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;

public class PrepareStatementImpl implements PrepareStatement {

    @Override
    public PreparedStatement prepare(PreparedStatement ps, LinkedHashMap<String, Object> values) throws SQLException {
        int index = 1;
        for (var value : values.values()) {
            switch (value) {
                case String v -> ps.setString(index, v);
                case Long v -> ps.setLong(index, v);
                case Timestamp v -> ps.setTimestamp(index, v);
                case Boolean v -> ps.setBoolean(index, v);
                case Integer v -> ps.setInt(index, v);
                case LocalDateTime v -> ps.setTimestamp(index, Timestamp.valueOf(v));
                case LocalDate v -> ps.setDate(index, Date.valueOf(v));
                case RoleName v -> ps.setString(index, v.getName());
                case Date v -> ps.setDate(index, v);
                case null, default -> ps.setObject(index, value);
            }
            index++;
        }

        return ps;
    }
}

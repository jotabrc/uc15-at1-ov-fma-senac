package io.github.jotabrc.util;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionUtil {

    Connection getCon() throws SQLException;
}

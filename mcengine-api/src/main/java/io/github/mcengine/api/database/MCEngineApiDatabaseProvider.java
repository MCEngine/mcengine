package io.github.mcengine.api.database;

import java.sql.Connection;

public interface MCEngineApiDatabaseProvider {
    Connection getConnection();

    void closeConnection(Connection connection);
}

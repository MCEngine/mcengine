package io.github.mcengine.api.database;

import java.sql.Connection;

/**
 * Interface for database providers in the MCEngine API.
 * This interface defines methods to manage database connections.
 */
public interface MCEngineApiDatabaseProvider {
    /**
     * Retrieves a database connection.
     * 
     * @return a {@link Connection} object representing the database connection.
     */
    Connection getConnection();

    /**
     * Closes the provided database connection.
     * 
     * @param connection the {@link Connection} object to be closed.
     *                   This should be a valid, open connection.
     */
    void closeConnection(Connection connection);
}

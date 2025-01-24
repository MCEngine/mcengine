package io.github.mcengine.api.database.sqlite;

import io.github.mcengine.api.database.MCEngineApiDatabaseProvider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A SQLite database provider implementation for the MCEngine API.
 * This class manages SQLite database connections and handles closing connections.
 */
public class MCEngineApiSQLITE implements MCEngineApiDatabaseProvider {
    private final String dbFilePath;

    /**
     * Constructs an instance of MCEngineApiSQLITE with the specified database file path.
     *
     * @param dbFilePath the path to the SQLite database file
     */
    public MCEngineApiSQLITE(String dbFilePath) {
        this.dbFilePath = dbFilePath;
    }

    /**
     * Establishes and returns a connection to the SQLite database.
     *
     * @return a {@link Connection} object to the database, or null if an error occurs
     */
    @Override
    public Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Closes the given database connection.
     *
     * @param connection the {@link Connection} object to close; does nothing if null
     */
    @Override
    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

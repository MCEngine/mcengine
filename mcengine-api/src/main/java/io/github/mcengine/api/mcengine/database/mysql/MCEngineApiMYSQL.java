package io.github.mcengine.api.mcengine.database.mysql;

import io.github.mcengine.api.mcengine.database.MCEngineApiDatabaseProvider;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A MySQL implementation of the MCEngineApiDatabaseProvider interface.
 * Provides methods to establish and close connections to a MySQL database.
 */
public class MCEngineApiMYSQL implements MCEngineApiDatabaseProvider {
    private final String host;
    private final String database;
    private final String username;
    private final String password;
    private final String port;

    /**
     * Constructor for MCEngineApiMYSQL.
     *
     * @param host     The hostname of the MySQL server.
     * @param database The name of the database.
     * @param username The username for the database.
     * @param password The password for the database.
     * @param port     The port number of the MySQL server.
     */
    public MCEngineApiMYSQL(String host, String database, String username, String password, String port) {
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
        this.port = port;
    }

    /**
     * Establishes and returns a connection to the MySQL database.
     *
     * @return A {@link Connection} object for the MySQL database, or null if a connection could not be established.
     */
    @Override
    public Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Closes the provided database connection.
     *
     * @param connection The {@link Connection} to be closed. If null, no action is taken.
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

package io.github.mcengine.api.database.mysql;

import io.github.mcengine.api.database.MCEngineApiDatabaseProvider;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MCEngineApiMYSQL implements MCEngineApiDatabaseProvider {
    private final String host;
    private final String database;
    private final String username;
    private final String password;
    private final String port;

    public MCEngineApiMYSQL(String host, String database, String username, String password, String port) {
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
        this.port = port;
    }

    @Override
    public Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

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

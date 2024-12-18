package io.github.mcengine.api.database.sqlite;

import io.github.mcengine.api.database.MCEngineApiDatabaseProvider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MCEngineApiSQLITE implements MCEngineApiDatabaseProvider {
    private final String dbFilePath;

    public MCEngineApiSQLITE(String dbFilePath) {
        this.dbFilePath = dbFilePath;
    }

    @Override
    public Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
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

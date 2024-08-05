package com.github.mcengine.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MCEngineApiSQLITE {
    public static Connection getConnection(String dbFilePath) {
        try {
            return DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

package io.github.mcengine.api.database;

import io.github.mcengine.api.database.mysql.MCEngineApiMYSQL;
import io.github.mcengine.api.database.sqlite.MCEngineApiSQLITE;

class MCEngineApiDatabase {
    private final MCEngineApiDatabaseProvider databaseProvider;

    public MCEngineApiDatabase(String dbType, String[] args) {
        switch (dbType.toLowerCase()) {
            case "sqlite":
                // Database file path
                this.databaseProvider = new MCEngineApiSQLITE(args[0]);
                break;
            case "mysql":
                // Host, Database, Username, Password, Port
                this.databaseProvider = new MCEngineApiMYSQL(args[0], args[1], args[2], args[3], args[4]);
                break;
            default:
                throw new IllegalArgumentException("Unsupported database type: " + dbType);
        }
    }

    public MCEngineApiDatabaseProvider getDatabaseProvider() {
        return databaseProvider;
    }
}
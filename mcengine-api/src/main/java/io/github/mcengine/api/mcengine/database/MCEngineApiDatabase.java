package io.github.mcengine.api.mcengine.database;

import io.github.mcengine.api.mcengine.database.mysql.MCEngineApiMYSQL;
import io.github.mcengine.api.mcengine.database.sqlite.MCEngineApiSQLITE;

/**
 * The {@code MCEngineApiDatabase} class is responsible for handling database connections
 * by selecting the appropriate database provider based on the specified database type.
 */
class MCEngineApiDatabase {
    private final MCEngineApiDatabaseProvider databaseProvider;

    /**
     * Constructs a new {@code MCEngineApiDatabase} instance with the specified database type and connection arguments.
     *
     * @param dbType The type of database to use. Supported values: "sqlite", "mysql".
     * @param args   The arguments required for the database connection:
     *               <ul>
     *                   <li>For SQLite: {@code args[0]} - The database file path.</li>
     *                   <li>For MySQL: {@code args[0]} - Host, {@code args[1]} - Database name,
     *                       {@code args[2]} - Username, {@code args[3]} - Password, {@code args[4]} - Port.</li>
     *               </ul>
     * @throws IllegalArgumentException if an unsupported database type is provided.
     */
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

    /**
     * Retrieves the database provider instance.
     *
     * @return The initialized database provider.
     */
    public MCEngineApiDatabaseProvider getDatabaseProvider() {
        return databaseProvider;
    }
}

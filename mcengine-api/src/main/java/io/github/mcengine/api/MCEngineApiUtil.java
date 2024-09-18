package io.github.mcengine.api;

import java.util.Optional;

import org.bukkit.plugin.java.JavaPlugin;

public class MCEngineApiUtil extends JavaPlugin {
    public static String getEnvOrConfig(JavaPlugin instance, String envVarName, String configKey) {
        try {
            String value = System.getenv(envVarName);
            return Optional.ofNullable(value).orElseGet(() -> {
                // Assuming instance.getConfig() returns a Config object with a getString method
                return instance.getConfig().getString(configKey);
            });
        } catch (Exception e) {
            // Handle exceptions gracefully, e.g., log the error and return a default value
            System.err.println("Error retrieving value: " + e.getMessage());
            return null; // Or return a default value based on your requirements
        }
    }

    public static Class<?> getClass(String className) throws ClassNotFoundException {
        // Return the Class object associated with the class name
        return Class.forName(className);
    }    
}
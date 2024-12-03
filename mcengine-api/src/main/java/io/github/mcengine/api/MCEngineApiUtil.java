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

    // For Clazz
    private Object initialize(String className, Object... constructorArgs) throws Exception {
        // Load the class
        Class<?> clazz = Class.forName(className);
    
        // Handle no arguments case
        if (constructorArgs == null || constructorArgs.length == 0) {
            return clazz.getConstructor().newInstance(); // No-arg constructor
        }
    
        // Determine parameter types
        Class<?>[] parameterTypes = new Class[constructorArgs.length];
        for (int i = 0; i < constructorArgs.length; i++) {
            parameterTypes[i] = mapWrapperToPrimitive(constructorArgs[i].getClass());
        }
    
        // Find and invoke the matching constructor
        return clazz.getConstructor(parameterTypes).newInstance(constructorArgs);
    }
    
    private void invokeMethod(Object instance, String methodName, Object... args) {
        try {
            // Determine parameter types
            Class<?>[] argTypes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                argTypes[i] = mapWrapperToPrimitive(args[i].getClass());
            }
    
            // Find and invoke the method
            instance.getClass().getMethod(methodName, argTypes).invoke(instance, args);
        } catch (Exception e) {
            throw new RuntimeException(
                "Error invoking method '" + methodName + "': " + e.getMessage(),
                e
            );
        }
    }
    
    // Map wrapper classes to primitives (or return the original class if no mapping is needed)
    private Class<?> mapWrapperToPrimitive(Class<?> clazz) {
        if (clazz == Double.class) return double.class;
        if (clazz == Integer.class) return int.class;
        if (clazz == Long.class) return long.class;
        if (clazz == Boolean.class) return boolean.class;
        if (clazz == Float.class) return float.class;
        if (clazz == Character.class) return char.class;
        if (clazz == Byte.class) return byte.class;
        if (clazz == Short.class) return short.class;
        return clazz; // Return the original class if no mapping is needed
    }      
}
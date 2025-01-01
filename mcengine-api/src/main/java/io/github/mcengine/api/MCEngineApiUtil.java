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

    /**
     * Initializes an instance of the specified class using the provided constructor arguments.
     *
     * @param className the fully qualified name of the class to initialize
     * @param constructorArgs the arguments to pass to the constructor; can be null or empty for a no-arg constructor
     * @return an instance of the specified class
     * @throws Exception if the class cannot be found, the constructor cannot be accessed, 
     *                   or instantiation fails for any reason
     */
    public static Object initialize(String className, Object... constructorArgs) throws Exception {
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

    /**
     * Invokes a method on the given instance with the specified arguments.
     *
     * @param instance   the object on which the method is to be invoked
     * @param methodName the name of the method to invoke
     * @param args       the arguments to pass to the method
     * @return the result of the method invocation
     * @throws RuntimeException if the method cannot be found, accessed, or invoked
     */
    public static Object invokeMethod(Object instance, String methodName, Object... args) {
        try {
            // Determine parameter types
            Class<?>[] argTypes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                argTypes[i] = mapWrapperToPrimitive(args[i].getClass());
            }

            // Find and invoke the method
            return instance.getClass().getMethod(methodName, argTypes).invoke(instance, args);
        } catch (Exception e) {
            throw new RuntimeException(
                "Error invoking method '" + methodName + "': " + e.getMessage(),
                e
            );
        }
    }
    
    /**
     * Maps a wrapper class to its corresponding primitive type.
     *
     * @param clazz the class to map
     * @return the corresponding primitive type, or the original class if no mapping exists
     */
    private static Class<?> mapWrapperToPrimitive(Class<?> clazz) {
        if (clazz == Double.class) return double.class;
        if (clazz == Integer.class) return int.class;
        if (clazz == Long.class) return long.class;
        if (clazz == Boolean.class) return boolean.class;
        if (clazz == Float.class) return float.class;
        if (clazz == Character.class) return char.class;
        if (clazz == Byte.class) return byte.class;
        if (clazz == Short.class) return short.class;
        return clazz;
    }      
}
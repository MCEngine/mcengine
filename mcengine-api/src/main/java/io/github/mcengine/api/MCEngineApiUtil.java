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
        Class<?> clazz = Class.forName(className);
    
        // Determine parameter types for the constructor
        Class<?>[] parameterTypes = new Class[constructorArgs.length];
        for (int i = 0; i < constructorArgs.length; i++) {
            parameterTypes[i] = mapWrapperToPrimitive(constructorArgs[i] != null ? constructorArgs[i].getClass() : Object.class);
        }
    
        // Find a matching constructor, if available
        try {
            return clazz.getConstructor(parameterTypes).newInstance(constructorArgs);
        } catch (NoSuchMethodException e) {
            // Attempt to find a more compatible constructor if direct match fails
            for (Constructor<?> constructor : clazz.getConstructors()) {
                if (isCompatible(constructor.getParameterTypes(), constructorArgs)) {
                    return constructor.newInstance(constructorArgs);
                }
            }
            throw new NoSuchMethodException("No suitable constructor found for class: " + className);
        }
    }
    
    private void invokeMethod(Object instance, String methodName, Object... args) {
        try {
            // Determine parameter types for the method
            Class<?>[] argTypes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                argTypes[i] = mapWrapperToPrimitive(args[i] != null ? args[i].getClass() : Object.class);
            }
    
            // Find the matching method and invoke it
            Method method = findCompatibleMethod(instance.getClass(), methodName, argTypes);
            if (method != null) {
                method.setAccessible(true);
                method.invoke(instance, args);
            } else {
                throw new NoSuchMethodException("No suitable method found: " + methodName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error invoking method '" + methodName + "': " + e.getMessage(), e);
        }
    }
    
    private Method findCompatibleMethod(Class<?> clazz, String methodName, Class<?>[] argTypes) {
        for (Method method : clazz.getMethods()) {
            if (method.getName().equals(methodName) && isCompatible(method.getParameterTypes(), argTypes)) {
                return method;
            }
        }
        return null;
    }
    
    private boolean isCompatible(Class<?>[] parameterTypes, Object[] args) {
        if (parameterTypes.length != args.length) {
            return false;
        }
        for (int i = 0; i < parameterTypes.length; i++) {
            if (args[i] != null && !parameterTypes[i].isAssignableFrom(mapWrapperToPrimitive(args[i].getClass()))) {
                return false;
            }
        }
        return true;
    }
    
    private Class<?> mapWrapperToPrimitive(Class<?> clazz) {
        if (clazz == Double.class) return double.class;
        if (clazz == Integer.class) return int.class;
        if (clazz == Long.class) return long.class;
        if (clazz == Boolean.class) return boolean.class;
        if (clazz == Float.class) return float.class;
        if (clazz == Character.class) return char.class;
        if (clazz == Byte.class) return byte.class;
        if (clazz == Short.class) return short.class;
        return clazz; // Return original if no mapping needed
    }    
}
package io.github.mcengine.api;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class MCEngineApiCommand {

    private final JavaPlugin plugin;
    private final Map<String, CommandExecutor> registeredCommands = new HashMap<>();

    public MCEngineApiCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Registers a new command with a permission and command executor.
     *
     * @param commandName  The name of the command (e.g., "greet")
     * @param permission   The permission required to execute the command (e.g., "pluginname.command.greet")
     * @param executor     The logic that runs when the command is executed
     */
    public void registerCommand(String commandName, String permission, CommandExecutor executor) {
        try {
            // Get the SimpleCommandMap from the server
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            SimpleCommandMap commandMap = (SimpleCommandMap) commandMapField.get(Bukkit.getServer());

            // Create the custom command
            MCEngineApiCommandCustom mcengineApiCommandCustom = new MCEngineApiCommandCustom(commandName, permission, executor);
            
            // Register the command with the server's command map
            commandMap.register(plugin.getDescription().getName(), mcengineApiCommandCustom);
            registeredCommands.put(commandName, executor);
            
            plugin.getLogger().info("Registered command: /" + commandName);
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to register command: /" + commandName);
            e.printStackTrace();
        }
    }
}
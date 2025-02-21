package io.github.mcengine.api.mcengine;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

import java.util.List;

/**
 * Custom implementation of a Bukkit command with permission and execution logic.
 * This class allows defining a custom command with specific permission requirements
 * and a custom executor for handling the command's behavior.
 */
public class MCEngineApiCommandCustom extends Command {

    private final CommandExecutor executor;
    private final String permission;

    /**
     * MCEngineApiCommandCustom constructor.
     *
     * @param name        The name of the command (e.g., "greet")
     * @param permission  The permission required to execute the command (e.g., "pluginname.command.greet")
     * @param executor    The command logic to be executed
     */
    protected MCEngineApiCommandCustom(String name, String permission, CommandExecutor executor) {
        super(name);
        this.permission = permission;
        this.executor = executor;
    }

    /**
     * Executes the command.
     *
     * @param sender        The source of the command.
     * @param commandLabel  The alias of the command used.
     * @param args          The arguments passed with the command.
     * @return True if the command was successfully executed; false otherwise.
     */
    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (permission != null && !sender.hasPermission(permission)) {
            sender.sendMessage("§cYou do not have permission to use this command.");
            return false;
        }

        return executor.onCommand(sender, this, commandLabel, args);
    }

    /**
     * Provides tab completion suggestions for the command.
     *
     * @param sender  The source of the command.
     * @param alias   The alias of the command used.
     * @param args    The arguments passed with the command.
     * @return A list of tab completion suggestions or an empty list if none are available.
     */
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        return super.tabComplete(sender, alias, args);
    }
}

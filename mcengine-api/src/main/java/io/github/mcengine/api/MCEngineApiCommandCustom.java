package io.github.mcengine.api;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

import java.util.List;

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

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (permission != null && !sender.hasPermission(permission)) {
            sender.sendMessage("§cYou do not have permission to use this command.");
            return false;
        }

        return executor.onCommand(sender, this, commandLabel, args);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        return super.tabComplete(sender, alias, args);
    }
}

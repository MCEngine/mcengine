package io.github.mcengine.mcengine.api;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.logging.Level;

/**
 * A generic API addon loader for Minecraft plugins.
 * <p>
 * This class facilitates loading and managing addons that implement a specified interface.
 *
 * @param <T> The type of the addon interface that all addons must implement.
 */
public class MCEngineApiAddon<T> {

    private final JavaPlugin plugin;
    private final Class<T> addonInterface;
    private final List<T> loadedAddons;

    /**
     * Constructor to initialize the MCEngineApiAddon.
     *
     * @param plugin          The plugin using the MCEngineApiAddon.
     * @param addonInterface  The interface all addons should implement.
     * @param loadedAddons    List to store loaded addons.
     */
    public MCEngineApiAddon(JavaPlugin plugin, Class<T> addonInterface, List<T> loadedAddons) {
        this.plugin = plugin;
        this.addonInterface = addonInterface;
        this.loadedAddons = loadedAddons;
    }

    /**
     * Loads addons from the specified folder.
     */
    public void loadAddons() {
        File addonDir = new File(plugin.getDataFolder(), "addons");
        if (!addonDir.exists() && !addonDir.mkdirs()) {
            plugin.getLogger().info("Addon folder could not be created: " + addonDir.getPath());
            return;
        }

        File[] files = addonDir.listFiles((dir, name) -> name.endsWith(".jar"));
        if (files == null || files.length == 0) {
            plugin.getLogger().info("No addons found in folder: " + addonDir.getPath());
            return;
        }

        for (File file : files) {
            try {
                URLClassLoader classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()}, plugin.getClass().getClassLoader());
                Class<?> addonClass = Class.forName(getAddonMainClassName(file), true, classLoader);
                if (addonInterface.isAssignableFrom(addonClass)) {
                    T addon = addonInterface.cast(addonClass.getDeclaredConstructor().newInstance());
                    loadedAddons.add(addon);
                    plugin.getLogger().info("Loaded addon: " + addonClass.getName());
                } else {
                    plugin.getLogger().warning("Addon " + file.getName() + " does not implement the required interface.");
                }
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to load addon: " + file.getName(), e);
            }
        }
    }

    /**
     * Gets the main class name of an addon JAR.
     * Override this method in case your addon JAR has specific metadata or custom logic.
     *
     * @param file The addon JAR file.
     * @return Fully qualified class name of the addon's main class.
     */
    protected String getAddonMainClassName(File file) {
        return "io.github.mcengine.addon.AddonImpl"; // Default main class for addons.
    }
}

package io.github.mcengine.api.mcengine;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

/**
 * A generic API addon loader for Minecraft plugins.
 * <p>
 * This class facilitates loading and managing addons that implement a specified interface.
 *
 * @param <T> The type of the addon interface that all addons must implement.
 */
public class MCEngineApiAddon<T extends MCEngineApiAddonInterface> {

    private final JavaPlugin plugin;
    private final Class<T> addonInterface;
    private final List<T> loadedAddons = new ArrayList<>();
    private final List<URLClassLoader> classLoaders = new ArrayList<>();

    /**
     * Constructor to initialize the MCEngineApiAddon.
     *
     * @param plugin         The plugin using the MCEngineApiAddon.
     * @param addonInterface The interface all addons should implement.
     */
    public MCEngineApiAddon(JavaPlugin plugin, Class<T> addonInterface) {
        this.plugin = plugin;
        this.addonInterface = addonInterface;
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
            try (URLClassLoader classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()}, plugin.getClass().getClassLoader())) {
                classLoaders.add(classLoader);

                String mainClassName = getAddonMainClassName(file);
                Class<?> addonClass = Class.forName(mainClassName, true, classLoader);
                if (addonInterface.isAssignableFrom(addonClass)) {
                    @SuppressWarnings("unchecked")
                    T addon = (T) addonClass.getDeclaredConstructor().newInstance();
                    loadedAddons.add(addon);
                    addon.onActivate();
                    plugin.getLogger().info("Loaded addon: " + addon.getName());
                } else {
                    plugin.getLogger().warning("Addon " + file.getName() + " does not implement the required interface.");
                }
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to load addon: " + file.getName(), e);
            }
        }
    }

    /**
     * Unloads all loaded addons and cleans up resources.
     */
    public void unloadAddons() {
        for (T addon : loadedAddons) {
            addon.onDeactivate();
            plugin.getLogger().info("Unloaded addon: " + addon.getName());
        }
        loadedAddons.clear();

        for (URLClassLoader classLoader : classLoaders) {
            try {
                classLoader.close();
            } catch (IOException e) {
                plugin.getLogger().log(Level.WARNING, "Failed to close class loader", e);
            }
        }
        classLoaders.clear();
    }

    /**
     * Reads the main class name from the addon JAR's plugin.yml.
     *
     * @param file The addon JAR file.
     * @return Fully qualified class name of the addon's main class.
     */
    protected String getAddonMainClassName(File file) {
        try (java.util.jar.JarFile jar = new java.util.jar.JarFile(file)) {
            java.util.jar.JarEntry entry = jar.getJarEntry("plugin.yml");
            if (entry == null) {
                return null;
            }
            try (java.io.InputStream input = jar.getInputStream(entry)) {
                Properties properties = new Properties();
                properties.load(input);
                return properties.getProperty("main");
            }
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to read plugin.yml from " + file.getName(), e);
        }
        return null;
    }
}

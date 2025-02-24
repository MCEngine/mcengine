package io.github.mcengine.api.mcengine;

/**
 * Interface that all MCEngine API addons must implement.
 */
public interface MCEngineApiAddonInterface {
    
    /**
     * Called when the addon is activated.
     */
    void onActivate();
    
    /**
     * Called when the addon is deactivated.
     */
    void onDeactivate();
    
    /**
     * Gets the name of the addon.
     * 
     * @return The name of the addon.
     */
    String getName();
}

package io.github.mcengine.api;


public interface MCEngineApiAddon {
    void onEnable(MCEngine plugin);
    void onDisable();
    String getName();
}

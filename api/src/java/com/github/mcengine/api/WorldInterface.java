package com.github.mcengine.api;

public interface WorldInterface {
    public void createWorld(String engine, String uuid, String seed);
    public void loadWorld(String engine, String worldUuid);
    public void unloadWorld(String engine, String worldUuid);
    public void deleteWorld(String engine, String worldUuid);
    public void saveWorld(String engine, String worldUuid);
    public void getWorld(String engine, String worldUuid);
    public void getWorlds(String engine);
}
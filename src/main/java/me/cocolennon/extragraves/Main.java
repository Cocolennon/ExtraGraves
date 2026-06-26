package me.cocolennon.extragraves;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("Plugin enabled!");
    }

    @Override
    public void onDisable() {
        instance = null;
        getLogger().info("Plugin disabled!");
    }

    public static Main getInstance() {
        return instance;
    }
}

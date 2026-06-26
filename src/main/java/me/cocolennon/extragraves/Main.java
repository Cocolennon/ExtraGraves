package me.cocolennon.extragraves;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Main instance;
    private Config config;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("Plugin enabled!");
    }

    public void loadConfig(boolean reload) {
        if(!reload) {
            saveDefaultConfig();
            getConfig().options().copyDefaults(true);
            saveConfig();
        }
        reloadConfig();
        config = new Config(this);
    }

    @Override
    public void onDisable() {
        instance = null;
        getLogger().info("Plugin disabled!");
    }

    public Config config() {
        return config;
    }

    public static Main getInstance() {
        return instance;
    }
}

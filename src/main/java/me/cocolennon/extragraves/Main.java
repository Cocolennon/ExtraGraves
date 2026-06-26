package me.cocolennon.extragraves;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.cocolennon.extragraves.commands.GravesCommand;
import me.cocolennon.extragraves.listeners.PlayerDeathListener;
import me.cocolennon.extragraves.listeners.PlayerInteractListener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Main instance;
    private Config config;

    @Override
    public void onEnable() {
        instance = this;
        loadConfig(false);
        registerCommands();
        registerListeners();
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

    private void registerCommands() {
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(GravesCommand.register());
        });
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), instance);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), instance);
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

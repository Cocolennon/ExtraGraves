package me.cocolennon.extragraves;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.cocolennon.extragraves.commands.GravesCommand;
import me.cocolennon.extragraves.listeners.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
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
        registerListener(new PlayerDeathListener(), instance);
        registerListener(new PlayerInteractListener(), instance);
        registerListener(new InventoryClickListener(), instance);
        registerListener(new InventoryCloseListener(), instance);
        registerListener(new BlockBreakListener(), instance);
        registerListener(new BlockExplodeListener(), instance);
    }

    private void registerListener(Listener listener, Plugin plugin) {
        getServer().getPluginManager().registerEvents(listener, plugin);
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

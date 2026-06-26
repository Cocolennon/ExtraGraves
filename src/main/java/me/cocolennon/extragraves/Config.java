package me.cocolennon.extragraves;

import me.cocolennon.extragraves.util.Localization;
import org.bukkit.configuration.file.FileConfiguration;

public final class Config {
    public final String defaultLocale;
    public final boolean autoUpdaterEnabled;
    public final String graveBlockName;
    public final boolean sendCoordinates;

    public Config(Main plugin) {
        FileConfiguration config = plugin.getConfig();
        this.defaultLocale = config.getString("defaultLocale");
        Localization.init(plugin, defaultLocale);
        this.autoUpdaterEnabled = config.getBoolean("auto-updater-enabled");
        this.graveBlockName = config.getString("grave-block");
        this.sendCoordinates = config.getBoolean("send-coordinates");
    }
}

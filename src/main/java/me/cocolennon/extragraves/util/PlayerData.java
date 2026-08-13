package me.cocolennon.extragraves.util;

import me.cocolennon.extragraves.Main;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.UUID;

public class PlayerData {
    private static YamlConfiguration getPlayerData(Player player) {
        UUID uuid = player.getUniqueId();
        File playerDataFolder = new File(Main.getInstance().getDataFolder(), "playerdata");
        File playerDataFile = new File(playerDataFolder, uuid + ".yml");
        return YamlConfiguration.loadConfiguration(playerDataFile);
    }

    private static void savePlayerData(Player player, YamlConfiguration playerData) {
        try {
            UUID uuid = player.getUniqueId();
            File playerDataFolder = new File(Main.getInstance().getDataFolder(), "playerdata");
            File playerDataFile = new File(playerDataFolder, uuid + ".yml");
            playerData.save(playerDataFile);
        }catch(Exception error){
            error.printStackTrace();
        }
    }

    public static void addGrave(Player player, Location location) {
        YamlConfiguration playerData = getPlayerData(player);
        long timestamp = System.currentTimeMillis();
        String gravePath = "grave." + timestamp + ".";
        playerData.set(gravePath + "world", location.getWorld().getName());
        playerData.set(gravePath + "x", location.getBlockX());
        playerData.set(gravePath + "y", location.getBlockY());
        playerData.set(gravePath + "z", location.getBlockZ());
        savePlayerData(player, playerData);
    }

    public static long getOldestGrave(Player player) {
        YamlConfiguration playerData = getPlayerData(player);
        ConfigurationSection graves = playerData.getConfigurationSection("grave");
        if(graves == null) return -1;
        long oldest = Long.MAX_VALUE;
        for(String key : graves.getKeys(false)) {
            long timestamp = Long.parseLong(key);
            if(timestamp < oldest) oldest = timestamp;
        }
        return oldest == Long.MAX_VALUE ? -1 : oldest;
    }

    public static Location getGraveLocation(Player player, long timestamp) {
        YamlConfiguration playerData = getPlayerData(player);
        ConfigurationSection graves = playerData.getConfigurationSection("grave");
        String gravePath =  "grave." + timestamp + ".";
        World world = Main.getInstance().getServer().getWorld(graves.getString("world"));
        return new Location(world, graves.getDouble(gravePath + "x"), graves.getDouble(gravePath + "y"), graves.getDouble(gravePath + "z"));
    }

    public static int getGraveCount(Player player) {
        YamlConfiguration playerData = getPlayerData(player);
        ConfigurationSection graves = playerData.getConfigurationSection("grave");
        return graves == null ? 0 : graves.getKeys(false).size();
    }

    public static void removeGrave(Player player, long timestamp) {
        YamlConfiguration playerData = getPlayerData(player);
        ConfigurationSection graves = playerData.getConfigurationSection("grave");
        graves.set(Long.toString(timestamp), null);
        savePlayerData(player, playerData);
    }
}

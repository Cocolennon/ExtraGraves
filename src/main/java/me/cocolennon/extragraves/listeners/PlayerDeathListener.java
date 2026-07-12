package me.cocolennon.extragraves.listeners;

import com.nexomc.nexo.api.NexoBlocks;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import dev.tins.worldguardextraflagsplus.flags.Flags;
import me.cocolennon.extragraves.Main;
import me.cocolennon.extragraves.util.Helper;
import me.cocolennon.extragraves.util.Localization;
import org.bg52.curiospaper.CuriosPaper;
import org.bg52.curiospaper.api.CuriosPaperAPI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PlayerDeathListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        if(player.hasMetadata("NPC")) return;
        if(!player.hasPermission("extragraves.grave-on-death")) return;
        Location deathLocation = player.getLocation();
        if(checkKeepInventoryRegion(deathLocation)) return;
        Location graveLocation = findFreeGraveLocation(deathLocation, 5);
        placeGrave(graveLocation);
        populateGrave(graveLocation, player);
        event.getDrops().clear();
        event.setDroppedExp(0);
        if(Main.getInstance().config().sendCoordinates) player.sendMessage(Localization.get(player, "death", true, (int) graveLocation.getX(), (int) graveLocation.getY(), (int) graveLocation.getZ()));
    }

    private boolean checkKeepInventoryRegion(Location location) {
        RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        Boolean value = query.queryValue(BukkitAdapter.adapt(location), null, Flags.KEEP_INVENTORY);
        return Boolean.TRUE.equals(value);
    }

    private void placeGrave(Location location) {
        String graveBlockName = Main.getInstance().config().graveBlockName;
        if(graveBlockName.startsWith("nexo-")) NexoBlocks.place(graveBlockName.replace("nexo-", ""), location);
        else location.getBlock().setType(Material.matchMaterial(graveBlockName));
    }

    public Location findFreeGraveLocation(Location center, int maxRadius) {
        World world = center.getWorld();
        int cx = center.getBlockX();
        int cy = center.getBlockY();
        int cz = center.getBlockZ();
        for(int r = 0; r <= maxRadius; r++) {
            for(int dx = -r; dx <= r; dx++) {
                for(int dz = -r; dz <= r; dz++) {
                    if(Math.max(Math.abs(dx), Math.abs(dz)) != r) continue;
                    for(int dy = -2; dy <= 2; dy++) {
                        Block block = world.getBlockAt(cx + dx, cy + dy, cz + dz);
                        if(isSafeForGrave(block)) return block.getLocation();
                    }
                }
            }
        }
        return center;
    }

    private boolean isSafeForGrave(Block block) {
        Material type = block.getType();
        if(!block.isEmpty() && !type.isAir() && !block.isReplaceable()) return false;
        return !(block.getState() instanceof TileState);
    }

    private void populateGrave(Location location, Player player) {
        Block grave = location.getBlock();
        Helper.setPlayer(grave, player);
        populateCurios(grave, player);
        Helper.setExperience(grave, player.getLevel(), player.getExp());
        List<ItemStack> armorItems = new ArrayList<>();
        for(ItemStack armorItem : player.getInventory().getArmorContents()) {
            if(armorItem == null) continue;
            armorItems.add(armorItem);
        }
        Helper.setArmor(grave, armorItems.toArray(ItemStack[]::new));
        Helper.setOffHand(grave, player.getInventory().getItemInOffHand());
        List<ItemStack> inventoryItems = new ArrayList<>();
        for(ItemStack inventoryItem : player.getInventory().getContents()) {
            if(inventoryItem == null) continue;
            if(armorItems.contains(inventoryItem)) continue;
            if(inventoryItem.equals(player.getInventory().getItemInOffHand())) continue;
            inventoryItems.add(inventoryItem);
        }
        Helper.setInventory(grave, inventoryItems.toArray(ItemStack[]::new));
    }

    private void populateCurios(Block grave, Player player) {
        CuriosPaperAPI curios = CuriosPaper.getInstance().getCuriosPaperAPI();
        List<ItemStack> curiosItems = new ArrayList<>();
        for(String slot : curios.getAllSlotTypes()) {
            List<ItemStack> equipped = curios.getEquippedItems(player, slot);
            if(equipped == null) continue;
            if(equipped.isEmpty()) continue;
            curiosItems.addAll(equipped);
        }
        for(String slot : curios.getAllSlotTypes()) {
            curios.clearEquippedItems(player, slot);
        }
        Helper.setCurios(grave, curiosItems);
    }
}

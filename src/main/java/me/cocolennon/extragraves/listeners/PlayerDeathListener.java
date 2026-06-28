package me.cocolennon.extragraves.listeners;

import com.nexomc.nexo.api.NexoBlocks;
import me.cocolennon.extragraves.Main;
import me.cocolennon.extragraves.util.Helper;
import me.cocolennon.extragraves.util.Localization;
import org.bg52.curiospaper.CuriosPaper;
import org.bg52.curiospaper.api.CuriosPaperAPI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
        if(!player.hasPermission("extragraves.grave-on-death")) return;
        Location deathLocation = player.getLocation();
        placeGrave(deathLocation);
        populateGrave(deathLocation, player);
        event.getDrops().clear();
        event.setDroppedExp(0);
        if(Main.getInstance().config().sendCoordinates) player.sendMessage(Localization.get(player, "death", true, (int) deathLocation.getX(), (int) deathLocation.getY(), (int) deathLocation.getZ()));
    }

    private void placeGrave(Location location) {
        String graveBlockName = Main.getInstance().config().graveBlockName;
        if(graveBlockName.startsWith("nexo-")) NexoBlocks.place(graveBlockName.replace("nexo-", ""), location);
        else location.getBlock().setType(Material.matchMaterial(graveBlockName));
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

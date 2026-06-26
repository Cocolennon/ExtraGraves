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
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        if(!player.hasPermission("extragraves.grave-on-death")) return;
        Location deathLocation = player.getLocation();
        placeGrave(deathLocation);
        populateGrave(deathLocation, player);
        event.getDrops().clear();
        event.setDroppedExp(0);
        if(Main.getInstance().config().sendCoordinates) player.sendMessage(Localization.get(player, "death", true, deathLocation.getX(), deathLocation.getY(), deathLocation.getZ()));
    }

    private void placeGrave(Location location) {
        String graveBlockName = Main.getInstance().config().graveBlockName;
        if(graveBlockName.startsWith("nexo-")) NexoBlocks.place(graveBlockName.replace("nexo-", ""), location);
        else location.getBlock().setType(Material.matchMaterial(graveBlockName));
    }

    private void populateGrave(Location location, Player player) {
        Block grave = location.getBlock();
        Helper.setPlayer(grave, player);
        Helper.setInventory(grave, player.getInventory().getContents());
        populateCurios(grave, player);
        Helper.setExperience(grave, player.getTotalExperience());
        Helper.setArmor(grave, player.getInventory().getArmorContents());
        Helper.setOffHand(grave, player.getInventory().getItemInOffHand());
    }

    private void populateCurios(Block grave, Player player) {
        CuriosPaperAPI curios = CuriosPaper.getInstance().getCuriosPaperAPI();
        List<ItemStack> curiosItems = new ArrayList<>();
        for(String slot : curios.getAllSlotTypes()) {
            List<ItemStack> equipped = curios.getEquippedItems(player, slot);
            if(equipped != null) {
                curiosItems.addAll(equipped);
                curios.clearEquippedItems(player, slot);
            }
        }
        Helper.setCurios(grave, curiosItems);
    }
}

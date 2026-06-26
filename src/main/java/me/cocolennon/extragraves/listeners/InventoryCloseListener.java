package me.cocolennon.extragraves.listeners;

import com.nexomc.nexo.api.NexoBlocks;
import me.cocolennon.extragraves.Main;
import me.cocolennon.extragraves.util.GraveHelper;
import me.cocolennon.extragraves.util.GraveInventoryHolder;
import me.cocolennon.extragraves.util.Helper;
import me.cocolennon.extragraves.util.Localization;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class InventoryCloseListener implements Listener {
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        if(!(inventory.getHolder() instanceof GraveInventoryHolder graveHolder)) return;
        Block grave =  graveHolder.getGrave();
        switch(graveHolder.getType()) {
            case INVENTORY -> GraveHelper.saveGrave(grave, inventory);
            case CURIOS -> GraveHelper.saveCurios(grave, inventory);
        }
        Helper.setGraveBusy(grave, false);
        if(!Helper.getInventory(grave).isEmpty()) return;
        if(!Helper.getArmor(grave).isEmpty()) return;
        if(!Helper.getOffHand(grave).getType().equals(Material.AIR)) return;
        if(!Helper.getCurios(grave).isEmpty()) return;
        if(Helper.getExperience(grave) != 0) return;
        if(Main.getInstance().config().graveBlockName.startsWith("nexo-")) NexoBlocks.remove(grave.getLocation());
        else grave.setType(Material.AIR);
        Player player = (Player) event.getPlayer();
        player.sendMessage(Localization.get(player, "grave-deleted", true));
    }
}

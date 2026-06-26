package me.cocolennon.extragraves.listeners;

import me.cocolennon.extragraves.util.GraveHelper;
import me.cocolennon.extragraves.util.GraveInventoryHolder;
import me.cocolennon.extragraves.util.Helper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class InventoryCloseListener implements Listener {
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        if(!(inventory.getHolder() instanceof GraveInventoryHolder graveHolder)) return;
        switch(graveHolder.getType()) {
            case INVENTORY -> GraveHelper.saveGrave(graveHolder.getGrave(), inventory);
            case CURIOS -> GraveHelper.saveCurios(graveHolder.getGrave(), inventory);
        }
        Helper.setGraveBusy(graveHolder.getGrave(), false);
    }
}

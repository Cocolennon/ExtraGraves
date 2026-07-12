package me.cocolennon.extragraves.listeners;

import com.nexomc.nexo.api.NexoBlocks;
import com.nexomc.nexo.utils.drops.Drop;
import me.cocolennon.extragraves.Main;
import me.cocolennon.extragraves.util.GraveHelper;
import me.cocolennon.extragraves.util.GraveInventoryHolder;
import me.cocolennon.extragraves.util.Helper;
import me.cocolennon.extragraves.util.Localization;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory clicked = event.getClickedInventory();
        if(clicked == null || !(clicked.getHolder() instanceof GraveInventoryHolder graveHolder)) return;
        ItemStack current = event.getCurrentItem();
        if(current == null || !current.hasItemMeta()) return;
        if(!Helper.hasButtonAction(current)) return;
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        Block grave = graveHolder.getGrave();
        switch(Helper.getButtonAction(current)) {
            case "openCurios" -> {
                GraveHelper.saveGrave(grave, clicked);
                GraveHelper.openCurios(player, grave);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            }
            case "experience" -> {
                clicked.setItem(52, new ItemStack(Material.AIR));
                float experience = Helper.getExperience(grave);
                player.setLevel(player.getLevel() + Helper.getLevel(grave));
                if(experience > 0.0) player.setExp(experience);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            }
            case "backToGrave" -> {
                GraveHelper.saveCurios(grave, clicked);
                GraveHelper.openGrave(player, grave);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            }
            case "destroy" -> {
                GraveHelper.dropGrave(grave, clicked);
                if(Main.getInstance().config().graveBlockName.startsWith("nexo-")) NexoBlocks.remove(grave.getLocation(), null, Drop.emptyDrop());
                else grave.setType(Material.AIR);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                player.sendMessage(Localization.get(player, "grave-destroyed", true));
            }
        }
    }
}

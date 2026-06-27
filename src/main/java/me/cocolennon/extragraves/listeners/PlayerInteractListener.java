package me.cocolennon.extragraves.listeners;

import me.cocolennon.extragraves.util.GraveHelper;
import me.cocolennon.extragraves.util.Helper;
import me.cocolennon.extragraves.util.Localization;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class PlayerInteractListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getHand() != EquipmentSlot.HAND) return;
        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        Player player = event.getPlayer();
        if(!player.isSneaking()) return;
        Block block = event.getClickedBlock();
        if(block == null || !Helper.isGrave(block)) return;
        if(Helper.isGraveBusy(block)) {
            player.sendMessage(Localization.get(player, "grave-busy", true));
            return;
        }
        Helper.setGraveBusy(block, true);
        GraveHelper.openGrave(player, block);
        player.playSound(player.getLocation(), Sound.BLOCK_BARREL_OPEN, 1.0f, 1.0f);
    }
}
package me.cocolennon.extragraves.listeners;

import me.cocolennon.extragraves.util.Helper;
import me.cocolennon.extragraves.util.Localization;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if(!Helper.isGrave(block)) return;
        Player player = event.getPlayer();
        player.sendMessage(Localization.get(player, "grave-break", true));
        event.setCancelled(true);
    }
}

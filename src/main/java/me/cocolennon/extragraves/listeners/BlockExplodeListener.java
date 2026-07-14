package me.cocolennon.extragraves.listeners;

import me.cocolennon.extragraves.util.GraveHelper;
import me.cocolennon.extragraves.util.Helper;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;

public class BlockExplodeListener implements Listener {
    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        Block block = event.getBlock();
        if(!Helper.isGrave(block)) return;
        GraveHelper.dropGrave(block);
    }
}

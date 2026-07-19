package me.cocolennon.extragraves.listeners;

import com.nexomc.nexo.api.NexoBlocks;
import com.nexomc.nexo.utils.drops.Drop;
import me.cocolennon.extragraves.Main;
import me.cocolennon.extragraves.util.GraveHelper;
import me.cocolennon.extragraves.util.Helper;
import me.cocolennon.extragraves.util.Localization;
import org.bukkit.Material;
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
        event.setDropItems(false);
        if(Main.getInstance().config().graveBlockName.startsWith("nexo-")) NexoBlocks.remove(block.getLocation(), null, Drop.emptyDrop());
        else block.setType(Material.AIR);
        GraveHelper.dropGrave(block);
        Player player = event.getPlayer();
        player.sendMessage(Localization.get(player, "grave-destroyed", true));
        event.setCancelled(true);
    }
}

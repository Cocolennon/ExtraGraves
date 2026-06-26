package me.cocolennon.extragraves.util;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class GraveHelper {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    public static void openGrave(Player player, Block grave) {
        Player graveOwner = Helper.getPlayer(grave);
        GraveInventoryHolder graveInventory = new GraveInventoryHolder(54, Localization.get(player, "menu-title", false, graveOwner.getName()), grave, GraveMenuType.INVENTORY);
        for(ItemStack inventoryItem : Helper.getInventory(grave)) graveInventory.addItem(inventoryItem);
        int armorSlot = 46;
        for(ItemStack armorItem : Helper.getArmor(grave)) {
            graveInventory.setItem(armorSlot, armorItem);
            armorSlot++;
        }
        ItemStack curiosItem = new ItemStackBuilder(Material.GOLD_INGOT, 1)
                .displayName(miniMessage.deserialize("<#FFFF55>Baubles"))
                .setKeyValue(Helper.buttonActionKey, PersistentDataType.STRING, "openCurios").get();
        graveInventory.setItem(40, curiosItem);
        graveInventory.setItem(51, Helper.getOffHand(grave));
        ItemStack experienceItem = new ItemStackBuilder(Material.EXPERIENCE_BOTTLE, 1)
                .displayName(miniMessage.deserialize("<#FFFF55>" + Helper.getExperience(grave) + " EXP"))
                .setKeyValue(Helper.buttonActionKey, PersistentDataType.STRING, "experience").get();
        graveInventory.setItem(52, experienceItem);
        graveInventory.fillEmpty(36);
        graveInventory.openInventory(player);
    }

    public static void openCurios(Player player, Block grave) {
        Player graveOwner = Helper.getPlayer(grave);
        GraveInventoryHolder curiosInventory = new GraveInventoryHolder(27, Localization.get(player, "curios-title", false, graveOwner.getName()), grave, GraveMenuType.CURIOS);
        int curiosSlot = 10;
        for(ItemStack curiosItem : Helper.getCurios(grave)) {
            curiosInventory.setItem(curiosSlot, curiosItem);
            if(curiosSlot == 12) curiosSlot += 2;
            else curiosSlot++;
        }
        ItemStack backToGrave = new ItemStackBuilder(Material.ARROW, 1)
                .displayName(miniMessage.deserialize("<#FFFF55>Back to Grave"))
                .setKeyValue(Helper.buttonActionKey, PersistentDataType.STRING, "backToGrave").get();
        curiosInventory.setItem(22, backToGrave);
        curiosInventory.fillEmpty(0);
        curiosInventory.openInventory(player);
    }
}

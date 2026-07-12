package me.cocolennon.extragraves.util;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

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
        ItemStack destroyItem = new ItemStackBuilder(Material.STONE_PICKAXE, 1)
                .displayName(Localization.get(player, "destroy-grave", false))
                .setKeyValue(Helper.buttonActionKey, PersistentDataType.STRING, "destroy").get();
        graveInventory.setItem(45, destroyItem);
        graveInventory.setItem(51, Helper.getOffHand(grave));
        ItemStack experienceItem = new ItemStackBuilder(Material.EXPERIENCE_BOTTLE, 1)
                .displayName(miniMessage.deserialize("<#FFFF55>" + Helper.getLevel(grave) + " Levels"))
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

    public static void saveGrave(Block grave, Inventory graveInventory) {
        List<ItemStack> inventoryItems = new ArrayList<>();
        for(int invSlot = 0; invSlot <= 35; invSlot++ ) {
            ItemStack item = graveInventory.getItem(invSlot);
            if(item == null) continue;
            inventoryItems.add(item);
        }
        Helper.setInventory(grave, inventoryItems.toArray(ItemStack[]::new));
        List<ItemStack> armorItems = new ArrayList<>();
        for(int armorSlot = 46; armorSlot <= 49; armorSlot++ ) {
            ItemStack item = graveInventory.getItem(armorSlot);
            if(item == null) continue;
            armorItems.add(item);
        }
        Helper.setArmor(grave, armorItems.toArray(ItemStack[]::new));
        ItemStack offHand = graveInventory.getItem(51);
        Helper.setOffHand(grave, offHand == null ? new ItemStack(Material.AIR) : offHand);
        ItemStack experienceItem = graveInventory.getItem(52);
        if(experienceItem == null || experienceItem.getType().equals(Material.AIR)) Helper.setExperience(grave, 0, 0.0f);
    }

    public static void saveCurios(Block grave, Inventory curiosInventory) {
        List<ItemStack> curiosItems = new ArrayList<>();
        for(int invSlot = 10; invSlot <= 16; invSlot++ ) {
            if(invSlot == 13) continue;
            ItemStack item = curiosInventory.getItem(invSlot);
            if(item == null) continue;
            curiosItems.add(item);
        }
        Helper.setCurios(grave, curiosItems);
    }

    public static void dropGrave(Block grave, Inventory graveInventory) {
        World world = grave.getWorld();
        Location location = grave.getLocation();
        for(int invSlot = 0; invSlot <= 35; invSlot++ ) {
            ItemStack item = graveInventory.getItem(invSlot);
            if(item == null) continue;
            world.dropItemNaturally(location, item);
        }
        for(int armorSlot = 46; armorSlot <= 49; armorSlot++ ) {
            ItemStack item = graveInventory.getItem(armorSlot);
            if(item == null || Helper.getButtonAction(item).equals("filler")) continue;
            world.dropItemNaturally(location, item);
        }
        ItemStack offhand = graveInventory.getItem(51);
        if(offhand != null && !Helper.getButtonAction(offhand).equals("filler")) world.dropItemNaturally(location, offhand);
        int totalXp = getExpToLevel(Helper.getLevel(grave));
        while(totalXp > 0) {
            int split = Math.min(totalXp, 500);
            ExperienceOrb orb = (ExperienceOrb) world.spawnEntity(location, EntityType.EXPERIENCE_ORB);
            orb.setExperience(split);
            totalXp -= split;
        }
        for(ItemStack curiosItem : Helper.getCurios(grave)) {
            if(curiosItem == null) continue;
            world.dropItemNaturally(location, curiosItem);
        }
    }

    private static int getExpToLevel(int level) {
        if (level <= 15) return level * level + 6 * level;
        if (level <= 30) return (int) (2.5 * level * level - 40.5 * level + 360);
        return (int) (4.5 * level * level - 162.5 * level + 2220);
    }
}

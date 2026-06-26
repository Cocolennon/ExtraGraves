package me.cocolennon.extragraves.util;

import com.jeff_media.customblockdata.CustomBlockData;
import com.jeff_media.morepersistentdatatypes.DataType;
import com.nexomc.nexo.api.NexoBlocks;
import me.cocolennon.extragraves.Main;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.*;

public class Helper {
    private static final Main main = Main.getInstance();
    public static final NamespacedKey buttonActionKey = new NamespacedKey(main, "buttonAction");
    private static final NamespacedKey playerUUIDKey = new NamespacedKey(main, "playerUUID");
    private static final NamespacedKey inventoryKey = new NamespacedKey(main, "inventory");
    private static final NamespacedKey curiosKey = new NamespacedKey(main, "curios");
    private static final NamespacedKey experienceKey = new NamespacedKey(main, "experience");
    private static final NamespacedKey armorKey = new NamespacedKey(main, "armor");
    private static final NamespacedKey offHandKey = new NamespacedKey(main, "offhand");

    public static boolean isGrave(Block block) {
        if(!NexoBlocks.isCustomBlock(block)) return false;
        PersistentDataContainer pdc = new CustomBlockData(block, main);
        return pdc.has(playerUUIDKey);
    }

    public static Player getPlayer(Block grave) {
        UUID uuid = new CustomBlockData(grave, main).get(playerUUIDKey, DataType.UUID);
        return Bukkit.getPlayer(uuid);
    }

    public static void setPlayer(Block grave, Player player) {
        UUID uuid = player.getUniqueId();
        new CustomBlockData(grave, main).set(playerUUIDKey, DataType.UUID, uuid);
    }

    public static List<ItemStack> getInventory(Block grave) {
        ItemStack[] rawInventory = new CustomBlockData(grave, main).get(inventoryKey, DataType.ITEM_STACK_ARRAY);
        return rawInventory == null ? Collections.emptyList() : Arrays.asList(rawInventory);
    }

    public static void setInventory(Block grave, ItemStack[] inventory) {
        new CustomBlockData(grave, main).set(inventoryKey, DataType.ITEM_STACK_ARRAY, inventory);
    }

    public static List<ItemStack> getCurios(Block grave) {
        ItemStack[] rawCurios = new CustomBlockData(grave, main).get(curiosKey, DataType.ITEM_STACK_ARRAY);
        return rawCurios == null ? Collections.emptyList() : Arrays.asList(rawCurios);
    }

    public static void setCurios(Block grave, List<ItemStack> curios) {
        new CustomBlockData(grave, main).set(curiosKey, DataType.ITEM_STACK_ARRAY, curios.toArray(ItemStack[]::new));
    }

    public static int getExperience(Block grave) {
        return new CustomBlockData(grave, main).get(experienceKey, DataType.INTEGER);
    }

    public static void setExperience(Block grave, Player player) {
        new CustomBlockData(grave, main).set(experienceKey, DataType.INTEGER, player.getTotalExperience());
    }

    public static List<ItemStack> getArmor(Block grave) {
        ItemStack[] rawArmor = new CustomBlockData(grave, main).get(armorKey, DataType.ITEM_STACK_ARRAY);
        return rawArmor == null ? Collections.emptyList() : Arrays.asList(rawArmor);
    }

    public static void setArmor(Block grave, ItemStack[] armor) {
        new CustomBlockData(grave, main).set(armorKey, DataType.ITEM_STACK_ARRAY, armor);
    }

    public static ItemStack getOffHand(Block grave) {
        return new CustomBlockData(grave, main).get(offHandKey, DataType.ITEM_STACK);
    }

    public static void setOffHand(Block grave, ItemStack offHand) {
        new CustomBlockData(grave, main).set(offHandKey, DataType.ITEM_STACK, offHand);
    }
}

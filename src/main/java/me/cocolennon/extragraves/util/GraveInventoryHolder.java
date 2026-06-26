package me.cocolennon.extragraves.util;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class GraveInventoryHolder implements InventoryHolder {
    private final Inventory inventory;
    private final int size;
    private final Block grave;
    private final GraveMenuType type;

    public GraveInventoryHolder(int size, Component title, Block grave, GraveMenuType type) {
        this.inventory = Bukkit.createInventory(this, size, title);
        this.size = size;
        this.grave = grave;
        this.type = type;
    }

    public void addItem(ItemStack item) {
        inventory.addItem(item);
    }

    public void setItem(int slot, ItemStack item) {
        inventory.setItem(slot, item);
    }

    public void fillEmpty(int startSlot) {
        ItemStack fillerItem = new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE, 1)
                .displayName(Component.text(" "))
                .setKeyValue(Helper.buttonActionKey, PersistentDataType.STRING, "filler").get();
        for(int i = startSlot; i < size; i++) if(inventory.getItem(i) == null) inventory.setItem(i, fillerItem);
    }

    public Block getGrave() {
        return grave;
    }

    public GraveMenuType getType() {
        return type;
    }

    public void openInventory(Player player) {
        player.openInventory(inventory);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}

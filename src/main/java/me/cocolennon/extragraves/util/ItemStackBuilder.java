package me.cocolennon.extragraves.util;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ItemStackBuilder {
    private final ItemStack itemStack;

    public ItemStackBuilder(Material material, int amount) {
        this.itemStack = new ItemStack(material, amount);
    }

    public ItemStackBuilder displayName(Component displayName) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(displayName);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public <P, C> ItemStackBuilder setKeyValue(NamespacedKey key, PersistentDataType<P, C> type, C value) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getPersistentDataContainer().set(key, type, value);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStack get() {
        return this.itemStack;
    }
}

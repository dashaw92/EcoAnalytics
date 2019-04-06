package me.daniel.eco.gui;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class ItemBuilder {

    public static ItemStack item(Material material, String name, String... lore) {
        return item(material, 1, name, lore);
    }
    
    
    public static ItemStack item(Material material, int amount, String name, String... lore) {
        ItemStack is = new ItemStack(material, amount);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        im.setLore(Arrays.asList(lore));
        is.setItemMeta(im);
        return is;
    }
}

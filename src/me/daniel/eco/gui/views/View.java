package me.daniel.eco.gui.views;

import org.bukkit.inventory.ItemStack;

import me.daniel.eco.gui.ViewerInventory;

public abstract class View {
    
    public abstract void build(ViewerInventory viewer);
    public abstract void onClick(ViewerInventory viewer, ItemStack item, int slot);
    
}

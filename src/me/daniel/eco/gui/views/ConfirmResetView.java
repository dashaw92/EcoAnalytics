package me.daniel.eco.gui.views;

import java.util.stream.IntStream;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.daniel.eco.EcoPlugin;
import me.daniel.eco.gui.ItemBuilder;
import me.daniel.eco.gui.ViewerInventory;
import net.md_5.bungee.api.ChatColor;

public final class ConfirmResetView extends View {

    @Override
    public void build(ViewerInventory viewer) {
        Inventory inv = viewer.getInventory();
        
        ItemStack cancel_item = ItemBuilder.item(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "Cancel");
        IntStream.range(0, inv.getSize()).forEach(i -> inv.setItem(i, cancel_item));
        inv.setItem(4, ItemBuilder.item(
                Material.BARRIER, 
                ChatColor.DARK_RED + "Confirm", 
                ChatColor.RED + "Click me to reset the data.", 
                "" + ChatColor.RED + ChatColor.ITALIC + "This cannot be undone."
        ));
    }

    @Override
    public void onClick(ViewerInventory viewer, ItemStack item, int slot) {
        if(item.getType() == Material.BARRIER) {
            EcoPlugin.data.reset(viewer.getViewer());
        }
        
        viewer.setView(new PageView());
        return;
    }

    
    
}

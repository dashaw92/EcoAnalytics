package me.daniel.eco.gui;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public final class GuiListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof ViewerInventory)) return;
        
        event.setCancelled(true);
        ViewerInventory inv = (ViewerInventory) event.getInventory().getHolder();

        if (event.getClick().isKeyboardClick() 
         || event.isShiftClick() 
         || event.getCurrentItem() == null
         || event.getCurrentItem().getType() == Material.AIR
         || !event.getClickedInventory().equals(inv.getInventory())) {
            return;
        }
        
        inv.onClick(event);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if(!(event.getInventory().getHolder() instanceof ViewerInventory)) return;
        ViewerInventory inv = (ViewerInventory) event.getInventory().getHolder();
        inv.onClose();
    }
}

package me.daniel.eco.hooks;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.daniel.eco.EcoPlugin;
import me.daniel.eco.gui.ViewerInventory;
import me.danny.essapi.EssItemSellEvent;

public final class SellHook implements Listener {
    
    @EventHandler
    public void onSell(EssItemSellEvent event) {
        EcoPlugin.getInstance().getData().update(event.getSoldItem(), event.getValue());
        ViewerInventory.refreshAll();
    }
    
}

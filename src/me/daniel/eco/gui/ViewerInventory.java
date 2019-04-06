package me.daniel.eco.gui;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import me.daniel.eco.gui.views.PageView;
import me.daniel.eco.gui.views.View;
import net.md_5.bungee.api.ChatColor;

public final class ViewerInventory implements InventoryHolder {

    private static final Map<Player, ViewerInventory> viewers = new HashMap<>(); 
    private static final int SIZE = 36;
    
    public static void onDisable() {
        viewers.keySet().forEach(Player::closeInventory);
        viewers.clear();
    }
    
    public static void refreshAll() {
        viewers.values().forEach(ViewerInventory::build);
    }
    
    private final Player viewer;
    private final Inventory inv;
    private View view;
    
    public ViewerInventory(Player player) {
        viewers.put(player, this);
        
        inv = Bukkit.createInventory(this, SIZE, ChatColor.DARK_RED + "EcoAnalytics Data Viewer");
        viewer = player;
        view = new PageView();
        
        build();
        viewer.openInventory(inv);
    }
    
    private void build() {
        inv.clear();
        view.build(this);
    }
    
    public void onClick(InventoryClickEvent event) {
        view.onClick(this, event.getCurrentItem(), event.getSlot());
        build();
    }
    
    public void onClose() {
        viewers.remove(viewer);
    }
    
    public void setView(View view) {
        this.view = view;
    }
    
    @Override
    public Inventory getInventory() {
        return inv;
    }
    
    public Player getViewer() {
        return viewer;
    }
}

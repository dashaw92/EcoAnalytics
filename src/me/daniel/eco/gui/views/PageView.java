package me.daniel.eco.gui.views;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.daniel.eco.EcoPlugin;
import me.daniel.eco.data.MaterialEntry;
import me.daniel.eco.gui.ItemBuilder;
import me.daniel.eco.gui.ViewerInventory;
import net.md_5.bungee.api.ChatColor;

public final class PageView extends View {

    private int page = 0;
    private static final NumberFormat VALUE_FORMAT = NumberFormat.getCurrencyInstance();

    @Override
    public void build(ViewerInventory viewer) {
        Inventory inv = viewer.getInventory();
        MaterialEntry[] materials = EcoPlugin.data.getAllEntries();

        int limit = inv.getSize() - 9;

        for (int i = 0; i < limit; i++) {
            int index = page * limit + i;
            if(index >= materials.length) break;
            MaterialEntry entry = materials[index];
            
            String lore1 = String.format("%sTotal sold: %s%d", ChatColor.YELLOW, ChatColor.LIGHT_PURPLE, entry.sold);
            String lore2 = String.format("%sTotal value: %s%s", ChatColor.YELLOW, ChatColor.LIGHT_PURPLE, VALUE_FORMAT.format(entry.value));
            inv.setItem(i, ItemBuilder.item(entry.material, ChatColor.GOLD + entry.material.name(), lore1, lore2));
        }
        
        String page_msg = String.format("%sPage %s%d%s/%s%d", 
                ChatColor.YELLOW, 
                ChatColor.LIGHT_PURPLE, 
                page + 1, 
                ChatColor.YELLOW, 
                ChatColor.LIGHT_PURPLE, 
                (EcoPlugin.data.getAmountOfKeys() / limit) + 1
        );
        
        inv.setItem(inv.getSize() - 9, ItemBuilder.item(Material.RED_STAINED_GLASS_PANE, ChatColor.DARK_RED + "Go back a page", page_msg));
        inv.setItem(inv.getSize() - 8, ItemBuilder.item(Material.LIME_STAINED_GLASS_PANE, ChatColor.DARK_GREEN + "Go forward a page", page_msg));
        inv.setItem(inv.getSize() - 5, getStatsItem(materials));
        
        if(viewer.getViewer().hasPermission("ntx.eco.reset")) {
            inv.setItem(inv.getSize() - 1, ItemBuilder.item(Material.BARRIER, ChatColor.RED + "Reset the data"));
        }
    }

    @Override
    public void onClick(ViewerInventory viewer, ItemStack item, int slot) {
        Inventory inv = viewer.getInventory();
        int limit = inv.getSize() - 9;
        if(limit == 0) limit = 1;
        
        if(slot < limit) return;
        if(item.getType() == Material.BARRIER && viewer.getViewer().hasPermission("ntx.eco.reset")) viewer.setView(new ConfirmResetView());
        if(item.getType() == Material.RED_STAINED_GLASS_PANE && page > 0) page--;
        if(item.getType() == Material.LIME_STAINED_GLASS_PANE && page < EcoPlugin.data.getAmountOfKeys() / limit) page++;
    }

    private static ItemStack getStatsItem(MaterialEntry[] items) {
        BigDecimal total_value = new BigDecimal(0);
        BigDecimal total_sold = new BigDecimal(0);
        MaterialEntry most_sold = null;
        
        for(MaterialEntry item : items) {
            if(most_sold == null || most_sold.sold < item.sold) most_sold = item;
            total_value = total_value.add(item.value);
            total_sold = total_sold.add(new BigDecimal(item.sold));
        }
        
        
        ItemStack stats = ItemBuilder.item(Material.PAPER, ChatColor.GOLD + "Stats");
        ItemMeta im = stats.getItemMeta();
        
        List<String> lore = new ArrayList<>();
        
        if(most_sold != null) {
            lore.add(ChatColor.YELLOW + "Item most sold: " + ChatColor.LIGHT_PURPLE + most_sold.material.name());
            lore.add("");
        }
        
        lore.add(ChatColor.YELLOW + "Total items sold: " + ChatColor.LIGHT_PURPLE + NumberFormat.getNumberInstance().format(total_sold));
        lore.add(ChatColor.YELLOW + "Total value of items: " + ChatColor.LIGHT_PURPLE + VALUE_FORMAT.format(total_value));
        
        im.setLore(lore);
        stats.setItemMeta(im);
        return stats;
    }
}

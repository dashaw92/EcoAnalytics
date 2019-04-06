package me.daniel.eco.hooks;

import java.math.BigDecimal;
import java.util.function.BiConsumer;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import com.earth2me.essentials.commands.Commandsell;

import me.daniel.eco.EcoPlugin;
import me.daniel.eco.gui.ViewerInventory;

public final class SellHook {
	
	private static int callback = -1;
	private static BiConsumer<ItemStack, BigDecimal> run = (i, m) -> {
	    EcoPlugin.data.update(i.getType(), i.getAmount(), m);
	    ViewerInventory.refreshAll();
	};
	
	public static void enable() {
		if(callback != -1) return;
		Bukkit.getConsoleSender().sendMessage("§e[EcoAnalytics] Hooking into Commandsell.");
		callback = Commandsell.onSell(run);
	}
	
	public static void disable() {
		if(callback == -1) return;
		Bukkit.getConsoleSender().sendMessage("§e[EcoAnalytics] Removing hook from Commandsell.");
		Commandsell.endCallback(callback);
	}
	
}

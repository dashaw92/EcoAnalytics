package me.daniel.eco.hooks;

import java.math.BigDecimal;
import java.util.function.BiConsumer;

import org.bukkit.inventory.ItemStack;

import com.earth2me.essentials.commands.Commandsell;

import me.daniel.eco.EcoPlugin;

public class SellHook {
	
	private static int callback = -1;
	private static BiConsumer<ItemStack, BigDecimal> run = (i, m) -> EcoPlugin.data.update(i.getType(), i.getAmount(), m);
	
	public static void enable() {
		if(callback != -1) return;
		System.out.println("[EcoAnalytics] Hooking into Commandsell.");
		callback = Commandsell.onSell(run);
	}
	
	public static void disable() {
		if(callback == -1) return;
		System.out.println("[EcoAnalytics] Removing hook from Commandsell.");
		Commandsell.endCallback(callback);
	}
	
}

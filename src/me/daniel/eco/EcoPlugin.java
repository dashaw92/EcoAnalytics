package me.daniel.eco;

import java.io.File;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.daniel.eco.commands.EcoResetCommand;
import me.daniel.eco.commands.EcoViewCommand;
import me.daniel.eco.data.ConfigApi;
import me.daniel.eco.gui.GuiListener;
import me.daniel.eco.gui.ViewerInventory;
import me.daniel.eco.hooks.SellHook;

public final class EcoPlugin extends JavaPlugin {

	public static EcoPlugin instance;
	public static ConfigApi data;
	
	@Override
	public void onEnable() {
		if(!doDannyChecks()) return;
		
		instance = this;
		data = new ConfigApi(new File(getDataFolder(), "data.yml"));
		SellHook.enable();
		
		Bukkit.getPluginManager().registerEvents(new GuiListener(), this);
		
		
		getCommand("eareset").setExecutor(new EcoResetCommand());
		getCommand("eadata").setExecutor(new EcoViewCommand());
	}
	
	@Override
	public void onDisable() {
		SellHook.disable();
		if(data != null) data.save();
		
		ViewerInventory.onDisable();
	}
	
	private boolean doDannyChecks() {
		if(Bukkit.getPluginManager().getPlugin("Essentials") == null) {
			getLogger().severe("Disabling because Essentials was not found.");
			setEnabled(false);
			return false;
		}
		
		if(!hasDannyEssentials()) {
			getLogger().severe("Disabling because the Essentials on the server is not modified by Danny.");
			setEnabled(false);
			return false;
		}
		
		return true;
	}
	
	private boolean hasDannyEssentials() {
		Class<?> clazz = com.earth2me.essentials.commands.Commandsell.class;
		Method[] methods = clazz.getMethods();
		
		for(Method method : methods) {
			if(method.getName().equalsIgnoreCase("onSell")) return true;
		}
		
		return false;
	}
}

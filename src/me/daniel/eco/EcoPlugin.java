package me.daniel.eco;

import java.io.File;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.daniel.eco.data.ConfigApi;
import me.daniel.eco.hooks.SellHook;

public class EcoPlugin extends JavaPlugin {

	public static EcoPlugin instance;
	public static ConfigApi data;
	
	@Override
	public void onEnable() {
		if(!doDannyChecks()) return;
		
		instance = this;
		data = new ConfigApi(new File(getDataFolder(), "data.yml"));
		SellHook.enable();
	}
	
	@Override
	public void onDisable() {
		SellHook.disable();
		if(data != null) data.save();
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

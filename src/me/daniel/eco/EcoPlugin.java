package me.daniel.eco;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.daniel.eco.commands.EcoResetCommand;
import me.daniel.eco.commands.EcoViewCommand;
import me.daniel.eco.data.ConfigApi;
import me.daniel.eco.gui.GuiListener;
import me.daniel.eco.gui.ViewerInventory;
import me.daniel.eco.hooks.SellHook;

public final class EcoPlugin extends JavaPlugin {

    private static EcoPlugin instance;
    private ConfigApi data;
    
    public static EcoPlugin getInstance() {
        return instance;
    }
    
    public ConfigApi getData() {
        return data;
    }
    
    @Override
    public void onEnable() {
        if(!doDannyChecks()) return;
        
        instance = this;
        data = new ConfigApi(new File(getDataFolder(), "data.yml"));
        
        Bukkit.getPluginManager().registerEvents(new GuiListener(), this);
        Bukkit.getPluginManager().registerEvents(new SellHook(), this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new DataSubscriptionTask(), DataSubscriptionTask.DELAY, DataSubscriptionTask.DELAY);
        
        getCommand("eareset").setExecutor(new EcoResetCommand());
        getCommand("eadata").setExecutor(new EcoViewCommand());
    }
    
    @Override
    public void onDisable() {
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
        try {
            Class.forName("me.danny.essapi.EssItemSellEvent");
            return true;
        } catch(ClassNotFoundException ex) {
            return false;
        }
    }
}

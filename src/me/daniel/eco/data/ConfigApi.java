package me.daniel.eco.data;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.daniel.eco.EcoPlugin;
import net.md_5.bungee.api.ChatColor;

public final class ConfigApi {
    
    private static final String MATERIAL_PATH = "%s";
    private static final String AMOUNT_PATH = "%s.total";
    private static final String VALUE_PATH = "%s.value";
    
    private FileConfiguration yml;
    private File file;
    
    public ConfigApi(File output) {
        file = output;
        checkFile();
        
        Arrays.stream(getAllEntries()).forEach(MaterialTracker::track);
    }
    
    private void checkFile() {
        file.getParentFile().mkdir();
        yml = YamlConfiguration.loadConfiguration(file);
    }
    
    public void update(Material material, int amount, BigDecimal value) {
        checkFile();
        
        MaterialTracker.track(new MaterialEntry(material, amount, value));
        
        String mat_path = String.format(MATERIAL_PATH, material.name());
        String amt_path = String.format(AMOUNT_PATH, mat_path);
        String val_path = String.format(VALUE_PATH, mat_path);
        
        if(!yml.contains(mat_path)) {
            yml.set(mat_path, material.name());
            yml.set(amt_path, amount);
            yml.set(val_path, value.toString());
        } else {
            int newAmt = amount + getAmount(amt_path);
            BigDecimal newVal = value.add(getValue(val_path));
            
            yml.set(amt_path, newAmt);
            yml.set(val_path, newVal.toString());
        }
        
        save();
    }
    
    public int getAmountOfKeys() {
        return yml.getKeys(false).size();
    }
    
    public MaterialEntry[] getAllEntries() {
        MaterialEntry[] arr = new MaterialEntry[getAmountOfKeys()];
        String[] keys = yml.getKeys(false).stream().sorted().toArray(String[]::new);
        int index = 0;
        
        for(String mat_name : keys) {
            Material material = Material.matchMaterial(mat_name);
            int amount_sold = getAmount(String.format(AMOUNT_PATH, mat_name));
            BigDecimal value = getValue(String.format(VALUE_PATH, mat_name));
            
            arr[index] = new MaterialEntry(material, amount_sold, value);
            index++;
        }
        
        return arr;
    }
    
    public void save() {
        try {
            yml.save(file);
        } catch(IOException e) {
            EcoPlugin.instance.getLogger().severe("Could not save to " + file.getAbsolutePath());
        }
    }
    
    public void reset(CommandSender sender) {
        file.delete();
        yml = YamlConfiguration.loadConfiguration(file);
        
        String name = ChatColor.RED + "[CONSOLE]";
        if(sender instanceof Player) {
            name = ((Player)sender).getDisplayName();
        }
        
        String msg = String.format("%s[EcoAnalytics] Data reset by %s%s.", ChatColor.YELLOW, name, ChatColor.YELLOW); 
        Bukkit.broadcast(msg, "ntx.eco");
        Bukkit.getConsoleSender().sendMessage(msg);
    }
    
    private int getAmount(String path) {
        if(!yml.contains(path)) return 0;
        return yml.getInt(path);
    }
    
    private BigDecimal getValue(String path) {
        if(!yml.contains(path)) return new BigDecimal(0);
        String val = yml.getString(path);
        return new BigDecimal(val);
    }
}

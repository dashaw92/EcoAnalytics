package me.daniel.eco.data;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.daniel.eco.EcoPlugin;

public class ConfigApi {
	
	private static final String MATERIAL_PATH = "%s";
	private static final String AMOUNT_PATH = "%s.total";
	private static final String VALUE_PATH = "%s.value";
	
	private FileConfiguration yml;
	private File file;
	
	public ConfigApi(File output) {
		file = output;
		checkFile();
	}
	
	private void checkFile() {
		file.getParentFile().mkdir();
		yml = YamlConfiguration.loadConfiguration(file);
	}
	
    public void update(Material material, int amount, BigDecimal value) {
    	checkFile();
    	
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
    
    public void save() {
    	try {
    		yml.save(file);
    	} catch(IOException e) {
    		EcoPlugin.instance.getLogger().severe("Could not save to " + file.getAbsolutePath());
    	}
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

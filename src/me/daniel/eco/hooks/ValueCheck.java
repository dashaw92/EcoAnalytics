package me.daniel.eco.hooks;

import java.math.BigDecimal;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.earth2me.essentials.Essentials;

public final class ValueCheck {

    public static final ValueCheck CHECKER = new ValueCheck();
    private Essentials ess;
    
    private ValueCheck() {
        ess = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
    }
    
    public BigDecimal valueOf(Material material) {
        if(ess == null) return BigDecimal.ZERO;
        
        BigDecimal worth = ess.getWorth().getPrice(ess, new ItemStack(material, 1));
        if(worth == null) return BigDecimal.ZERO;
        return worth;
    }
    
}

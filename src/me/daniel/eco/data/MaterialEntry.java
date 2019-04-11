package me.daniel.eco.data;

import java.math.BigDecimal;

import org.bukkit.Material;

public final class MaterialEntry {

    public final Material material;
    public final int sold;
    public final BigDecimal value;
    
    protected MaterialEntry(Material material, int sold, BigDecimal value) {
        this.material = material;
        this.sold = sold;
        this.value = value;
    }
    
}

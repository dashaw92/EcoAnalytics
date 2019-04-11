package me.daniel.eco.data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;

public final class MaterialTracker {
    
    private static final Map<Material, MaterialDelta> deltas = new HashMap<>();
    
    public static void track(MaterialEntry material) {
        if(!deltas.containsKey(material.material)) {
            deltas.put(material.material, new MaterialDelta(material));
            return;
        }
        
        deltas.get(material.material).update(material);
    }
    
    public static MaterialDelta[] getDeltas() {
        return deltas.values().toArray(new MaterialDelta[0]);
    }
    
    public static void resetDeltas() {
        deltas.values().forEach(MaterialDelta::reset);
    }
    
    private MaterialTracker() {}
     
    public static class MaterialDelta {
        private final Material material;
        
        public BigDecimal original_value, current_value;
        public int original_sold, current_sold;
        
        private MaterialDelta(MaterialEntry material) {
            this.material = material.material;
            
            this.original_value = material.value;
            this.original_sold = material.sold;
            
            this.current_value = material.value;
            this.current_sold  = material.sold;
        }
        
        public Material getMaterial() {
            return material;
        }
        
        public void update(MaterialEntry entry) {
            if(material != entry.material) return;
            
            current_value = current_value.add(entry.value);
            current_sold += entry.sold;
        }
        
        public int getSoldDelta() {
            return current_sold - original_sold;
        }
        
        public BigDecimal getValueDelta() {
            return current_value.subtract(original_value);
        }
        
        private void reset() {
            original_value = current_value;
            original_sold = current_sold;
        }
    }
}

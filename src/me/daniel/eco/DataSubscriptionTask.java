package me.daniel.eco;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;

import me.daniel.eco.data.MaterialTracker;
import me.daniel.eco.data.MaterialTracker.MaterialDelta;
import me.daniel.eco.data.Subscriber;

public final class DataSubscriptionTask implements Runnable {
    
    public static final long DELAY = 5 * 60 * 20;
    
    protected DataSubscriptionTask() {}
    
    public void run() {
        MaterialDelta[] deltas = MaterialTracker.getDeltas();
        Map<Material, String> baked = new HashMap<>();
        for(MaterialDelta delta : deltas) {
            baked.put(delta.getMaterial(), getMessageFor(delta));
        }
        
        for(Subscriber sub : Subscriber.getAll()) {
            baked.keySet()
                 .stream()
                 .filter(sub::isSubbed)
                 .forEach(mat -> sub.tell(baked.get(mat)));
        }
        
        MaterialTracker.resetDeltas();
    }
    
    private String getMessageFor(MaterialDelta delta) {
        String prefix = "§f[Eco] ";
        String format_add = "%s§7%s§f - §a+§2%d sold§7, §a+§2$%s";
        String format_same = "%s§7%s§f - §cNo changes";
        
        if(delta.getSoldDelta() < 1) return String.format(format_same, prefix, delta.getMaterial().name());
        
        return String.format(format_add,
                             prefix,
                             delta.getMaterial().name(), 
                             delta.getSoldDelta(), 
                             NumberFormat.getNumberInstance().format(delta.getValueDelta())
               );
    }
}

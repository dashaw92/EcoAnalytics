package me.daniel.eco.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public final class Subscriber {

    private static final int SUBSCRIPTION_LIMIT = 5;
    private static final Map<UUID, Subscriber> subscribers = new HashMap<>();
    
    public static Subscriber get(Player player) {
        if(!subscribers.containsKey(player.getUniqueId())) subscribers.put(player.getUniqueId(), new Subscriber(player));
        return subscribers.get(player.getUniqueId());
    }

    public static Subscriber[] getAll() {
        return subscribers.values().toArray(new Subscriber[0]);
    }
    
    private final UUID uuid;
    private final List<Material> subscriptions = new ArrayList<>();
    
    private Subscriber(Player player) {
        this.uuid = player.getUniqueId();
        subscribers.put(player.getUniqueId(), this);
    }
    
    public SubscriptionStatus sub(Material mat) {
        if(subscriptions.size() >= SUBSCRIPTION_LIMIT) return SubscriptionStatus.MAX_SUBSCRIPTIONS;
        
        if(!subscriptions.contains(mat)) {
            subscriptions.add(mat);
            return SubscriptionStatus.SUCCESS_SUB;
        }
        
        return SubscriptionStatus.ALREADY_SUBSCRIBED;
    }
    
    public void unsub(Material mat) {
        subscriptions.remove(mat);
    }
    
    public SubscriptionStatus toggleSub(Material mat) {
        if(isSubbed(mat)) {
            unsub(mat);
            return SubscriptionStatus.SUCCESS_UNSUB;
        }
        
        return sub(mat);
    }
    
    public boolean isSubbed(Material material) {
        return subscriptions.contains(material);
    }
    
    public void tell(String msg) {
        if(!Bukkit.getOfflinePlayer(uuid).isOnline()) return;
        Bukkit.getPlayer(uuid).sendMessage(msg);
    }
    
    public static enum SubscriptionStatus {
        MAX_SUBSCRIPTIONS, SUCCESS_UNSUB, ALREADY_SUBSCRIBED, SUCCESS_SUB;
    }
}

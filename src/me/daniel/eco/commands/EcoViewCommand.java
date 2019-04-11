package me.daniel.eco.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.daniel.eco.gui.ViewerInventory;
import net.md_5.bungee.api.ChatColor;

public final class EcoViewCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player) || !sender.hasPermission("ntx.eco")) {
            sender.sendMessage(ChatColor.RED + "You lack permission.");
            return true;
        }
        
        new ViewerInventory((Player)sender);
        return true;
    }
    
}

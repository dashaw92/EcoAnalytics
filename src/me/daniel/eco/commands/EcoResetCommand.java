package me.daniel.eco.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.daniel.eco.EcoPlugin;

public final class EcoResetCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!sender.hasPermission("ntx.eco.reset")) {
            sender.sendMessage("§cYou lack permission.");
            return true;
        }
        
        EcoPlugin.getInstance().getData().reset(sender);
        return true;
    }
    
}

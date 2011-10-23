package de.qgel.skySMP;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveIslandCommand implements CommandExecutor {
    private final skySMP plugin;

    public RemoveIslandCommand(skySMP plugin) {
        this.plugin = plugin;
    }
    
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        if (!(sender instanceof Player) || !(sender.isOp()) ){
            return false;
        }
        
        if(split.length == 1) {
        	String playerName = split[0];
        	if(plugin.hasIsland(playerName)) {
        		plugin.deleteIsland(playerName, ((Player) sender).getWorld());
        		return true;
        	} else {
        		sender.sendMessage("Player \"" + playerName + "\" doesn't have an island registered.");
        		return true;
        	}
        	
        }
		
		return false;
	}

}

package de.qgel.skySMP;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class skyDevCommand implements CommandExecutor {
    private final skySMP plugin;

    public skyDevCommand(skySMP plugin) {
        this.plugin = plugin;
    }
    
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        if (!(sender instanceof Player) || !(sender.isOp()) ){
            return false;
        }
		sender.sendMessage(plugin.getLastIsland().x + " / " +plugin.getLastIsland().z);
		return true;
	}

}

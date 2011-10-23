package de.qgel.skySMP;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class tpHomeCommand implements CommandExecutor {
    private final skySMP plugin;

    public tpHomeCommand(skySMP plugin) {
        this.plugin = plugin;
    }
    
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        if (!(sender instanceof Player) ){
            return false;
        }
        Player player = (Player)sender;
		if(plugin.hasIsland(sender.getName())) {
			if(player.getWorld().getEnvironment().getId() == 0) {
				plugin.teleportHome(((Player)sender));
				return true;
			} else {
				player.sendMessage("Can't tphome in the nether, sorry");
				return true;
			}
		} else {
			sender.sendMessage("You don't have an Island, I can't send you home.");
			return true;
		}
	}

}

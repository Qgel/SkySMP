package de.qgel.skySMP;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class skyHelpCommand implements CommandExecutor {
    
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        if (!(sender instanceof Player) ){
            return false;
        }
        if (split.length == 0) {
        	sender.sendMessage("skySMP is a mod that gives you a (very) small map in the sky. Survive the best you can there!" );
        	sender.sendMessage("Commands:");
        	sender.sendMessage("/newIsland [replace]: gives you a new Island (and starting items) or replaces your old one");
        	sender.sendMessage("/tphome : Teleports you to your island.");
        	if(sender.isOp())
        		sender.sendMessage("/removeIsland <playername> : remove the Island of a given player");
        	sender.sendMessage("/skyhelp : Print this help message");
        } else if (split.length > 0) {
        	sender.sendMessage("These are the challenges:");
        	sender.sendMessage("1.Build a Cobble Stone generator.");
        	sender.sendMessage("2.Build a house.");
        	sender.sendMessage("3.Expand the island.");
        	sender.sendMessage("4.Make a melon farm.");
        	sender.sendMessage("5.Make a pumpkin farm.");
        	sender.sendMessage("6.Make a reed farm.");
        	sender.sendMessage("7.Make a wheat farm.");
        	sender.sendMessage("8.Make a giant red mushroom. ");
        	sender.sendMessage("9.Build a bed.");
        	sender.sendMessage("10.Make 40 stone brick's. ");
        	sender.sendMessage("11.Make atleast 20 torches.");
        	sender.sendMessage("12.Make an infinite water source. ");
        	sender.sendMessage("13.Build a furnace.");
        	sender.sendMessage("14.Make a small lake.");
        	sender.sendMessage("15.Make a platform 24 blocks away from the island, for mobs to spawn. ");
        	sender.sendMessage("16.Make 10 cactus green dye. ");
        	sender.sendMessage("17.Make 10 mushroom stew. ");
        	sender.sendMessage("18.Make 10 Jack 'o' lanterns. ");
        	sender.sendMessage("19.Build 10 bookcases.");
        	sender.sendMessage("20.Make 10 bread.");
        	sender.sendMessage("SMP challanges:");
        	sender.sendMessage("21.Connect to another Island");
        }
		return true;
	}

}

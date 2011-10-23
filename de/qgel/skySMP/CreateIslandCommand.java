package de.qgel.skySMP;


import java.util.Iterator;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
* Handler for the /newIsland command
* @author Qgel
*/
public class CreateIslandCommand implements CommandExecutor {
    private final skySMP plugin;

    public CreateIslandCommand(skySMP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;
        
       
        if(!(player.getWorld().getEnvironment().getId() == 0)) {
        	player.sendMessage("Can only do that in the normal world, sorry");
        	return true;
        }

        if (plugin.hasIsland(player)) {
        	if(split.length == 0) {
        		Island location = plugin.getPlayerIsland(player.getName());
        		player.sendMessage("You already have an Island at " + location.x +" / " + location.z + " If you want a new one, use \"/newIsland replace\" instead.");
        		return true;
        	} else if (split[0].equals("replace")) {
        		plugin.deleteIsland(player.getName(),player.getWorld());  
        		player.getInventory().clear();
        		
        		//remove Items that drop on the island due to removal
        		List<Entity> Entities = player.getNearbyEntities(15,15,15);
        		Iterator<Entity> ent = Entities.iterator();
        		while (ent.hasNext())
        			ent.next().remove();
        		
        		return createIsland(player);
        	} else if (split[0].equals("override")) {
        		if(!(player.isOp()))
        			return false;
        		return createIsland(player);
        	}
        } else {
            	return createIsland(player);
        } 
        return false;
    }

	private boolean createIsland(Player player) {
		Island last = plugin.getLastIsland();
		try {	
			Island next;
    		//if we have space because of a deleted Island, create one there
    		if(plugin.hasOrphanedIsland()) {
    			next = plugin.getOrphanedIsland();
    		} else {
                next = nextIslandLocation(last);
                plugin.setLastIsland(next);
    		}
		
            generateIslandBlocks(next.x, next.z, player);
            plugin.registerPlayerIsland(player, next);
            plugin.teleportHome(player);
            
        } catch (Exception ex) {
            player.sendMessage("Could not create your Island. Pleace contact a server moderator.");
            plugin.setLastIsland(last);
            ex.printStackTrace();
            return false;
        }
        return true;	
	}

	public void generateIslandBlocks(int x, int z, Player player ){  
        int y = plugin.getISLANDS_Y(); //blub
       
        for(int x_operate = x; x_operate < x+3; x_operate++){
                for(int y_operate = y; y_operate < y+3; y_operate++){
                        for(int z_operate = z; z_operate < z+6; z_operate++){
                                Block blockToChange = player.getWorld().getBlockAt(x_operate,y_operate,z_operate);
                                blockToChange.setTypeId(2);  //chest area                  
                        }
                }
        }
       
        for(int x_operate = x+3; x_operate < x+6; x_operate++){
                for(int y_operate = y; y_operate < y+3; y_operate++){
                        for(int z_operate = z+3; z_operate < z+6; z_operate++){
                                Block blockToChange = player.getWorld().getBlockAt(x_operate,y_operate,z_operate);
                                blockToChange.setTypeId(2);    // 3x3 corner       
                        }
                }
        }
       
        //tree
        for(int x_operate = x+3; x_operate < x + 7; x_operate++){
                for(int y_operate = y+7; y_operate < y+10; y_operate++){
                        for(int z_operate = z+3; z_operate < z+7; z_operate++){
                                Block blockToChange = player.getWorld().getBlockAt(x_operate,y_operate,z_operate);
                                blockToChange.setTypeId(18);    //leaves   
                        }
                }
        }
       
       
        for(int y_operate = y+3; y_operate < y+9; y_operate++){
                Block blockToChange = player.getWorld().getBlockAt(x+5,y_operate,z+5);
                blockToChange.setTypeId(17); 
        }
       
       
        // chest
        Block blockToChange = player.getWorld().getBlockAt(x+1,y+3,z+1);
        blockToChange.setTypeId(54);
        Chest chest = (Chest) blockToChange.getState();
        Inventory inventory = chest.getInventory();
        
        ItemStack item = new ItemStack(287,12); //String
        inventory.addItem(item);
        item = new ItemStack(327,1); //Bucket lava
        inventory.addItem(item);
        item = new ItemStack(352,1); //Bone
        inventory.addItem(item);
        item = new ItemStack(338,1); //Sugar Cane
        inventory.addItem(item);
        item = new ItemStack(40,1); //Mushroom red
        inventory.addItem(item);
        item = new ItemStack(79,2); //Ice
        inventory.addItem(item);
        item = new ItemStack(361,1); //pumpkin seeds
        inventory.addItem(item);
        item = new ItemStack(39,1); //mushroom brown
        inventory.addItem(item);
        item = new ItemStack(360,1); //melon slice
        inventory.addItem(item);
        item = new ItemStack(81,1); //cactus
        inventory.addItem(item);
        
       
        //spawn
        blockToChange = player.getWorld().getBlockAt(x,y,z);
        blockToChange.setTypeId(7);
       
        //sand
        blockToChange = player.getWorld().getBlockAt(x+2,y+1,z+1);
        blockToChange.setTypeId(12);
        blockToChange = player.getWorld().getBlockAt(x+2,y+1,z+2);
        blockToChange.setTypeId(12);
        blockToChange = player.getWorld().getBlockAt(x+2,y+1,z+3);
        blockToChange.setTypeId(12);
       
	}

	private Island nextIslandLocation(Island lastIsland) {
		// Gets the next position of an Island based on the last one.
		
		// Generates new Islands in a spiral.
		int x = lastIsland.x;
		int z = lastIsland.z;
		 Island nextPos = new Island();
		 nextPos.x = x;
		 nextPos.z = z;
		    if ( x < z )
		    {
		        if ( ((-1) * x) < z) 
		        {
		           nextPos.x = nextPos.x + plugin.getISLAND_SPACING();
		           return nextPos;
		        }
		        nextPos.z = nextPos.z + plugin.getISLAND_SPACING();
		        return nextPos;
		    }
		    if( x > z)
		    {
		        if ( ((-1) * x) >= z)
		        {
		            nextPos.x = nextPos.x - plugin.getISLAND_SPACING();
		            return nextPos;
		        }
		            nextPos.z = nextPos.z - plugin.getISLAND_SPACING();
		            return nextPos;
		    } 
		    if ( x <= 0)
		    {
		    	nextPos.z = nextPos.z + plugin.getISLAND_SPACING();
		        return nextPos;
		    }
		    nextPos.z = nextPos.z - plugin.getISLAND_SPACING();
		    return nextPos;
	}
}
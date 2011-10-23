/**
* Skyblock SMP mod
* @author Qgel
* Original idea and map by Noobcrew
* http://www.minecraftforum.net/topic/600254-surv-skyblock/
*/

package de.qgel.skySMP;

import java.io.File;
import java.util.HashMap;
import java.util.Stack;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class skySMP extends JavaPlugin {
    private final PlayerEventListener playerListener = new PlayerEventListener(this);
    private HashMap<String, Island> playerIslands = new HashMap<String, Island>();
    private Stack<Island> orphaned = new Stack<Island>();

    private static int SPAWN_X = 0;
    private static int SPAWN_Z = 0;
    private Island lastIsland;
    private static int ISLANDS_Y = 100;
    private static int ISLAND_SPACING = 100;

    public void onDisable() {
        //save out IslandData to disk
    	try {
			SLAPI.save(playerIslands, "playerIslands.bin");
			SLAPI.save(lastIsland, "lastIsland.bin");
			SLAPI.save(orphaned, "orpahnedIslands.bin");
		} catch (Exception e) {
			System.out.println("Something went wrong saving the Island data. That's really bad but there is nothing we can really do about it. Sorry");
			e.printStackTrace();
		}
    	PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is now Disabled!" );
    }

	@Override
	public void onEnable() {

        // Register events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_RESPAWN, playerListener, Priority.Normal, this);

        // Register commands
        getCommand("newIsland").setExecutor(new CreateIslandCommand(this));
        getCommand("removeIsland").setExecutor(new RemoveIslandCommand(this));
        getCommand("tphome").setExecutor(new tpHomeCommand(this));
        getCommand("skyHelp").setExecutor(new skyHelpCommand());
        getCommand("skydev").setExecutor(new skyDevCommand(this));
        
        PluginDescriptionFile pdfFile = this.getDescription();
        
        //Load the Island data from disk
        try { 
        	if(new File("lastIsland.bin").exists())
        		lastIsland = (Island)SLAPI.load("lastIsland.bin");
        	
        	if(null == lastIsland) {
            	//in case we don't have any data on disk
            	lastIsland = new Island(); 
            	lastIsland.x = 0;
            	lastIsland.z = 0;
        	}
        	
        	//playerIslands = new HashMap<String, Island>();
        	if(new File("playerIslands.bin").exists()){
            	@SuppressWarnings("unchecked")
        		HashMap<String,Island> load = (HashMap<String,Island>)SLAPI.load("playerIslands.bin");
    			if(null != load) 
    				playerIslands = load;
        	}
        	
        	if(new File("orphanedIslands.bin").exists()) {
        		@SuppressWarnings("unchecked")
				Stack<Island> load = (Stack<Island>)SLAPI.load("orphanedIslands.bin");
				if(null != load)
					orphaned = load;
			}
        
        } catch (Exception e) {
			System.out.println("Could not load Island data from disk.");
			e.printStackTrace();
        }
        
        makeSpawn("skyIsland");
        
        System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
    }
    
    public boolean hasIsland(final Player player) {
    	return playerIslands.containsKey(player.getName());
    }
    public boolean hasIsland(String playername) {
    	return playerIslands.containsKey(playername);
    }
    
    public boolean hasOrphanedIsland() {
    	return !orphaned.empty();
    }
    public Island getOrphanedIsland() {
    	if(hasOrphanedIsland()) {
    		return orphaned.pop();
    	}
    	
    	Island spawn = new Island();
    	spawn.x = SPAWN_X;
    	spawn.z = SPAWN_Z;
    	
    	return spawn;
    }
    
    public Island getPlayerIsland(String playerName) {
    	if(hasIsland(playerName)) {
    		return playerIslands.get(playerName);
    	} 
    	Island spawn = new Island();
    	spawn.x = SPAWN_X;
    	spawn.z = SPAWN_Z;
    	return spawn;
    }
    
    public int getISLANDS_Y() { return ISLANDS_Y; };
    public Island getLastIsland() { return lastIsland; }
    public void setLastIsland(Island island) { lastIsland = island; }
    public int getISLAND_SPACING() { return ISLAND_SPACING; }

	public void deleteIsland(String playerName,World world) {
		if(hasIsland(playerName)) {
			Island island = getPlayerIsland(playerName);
			for(int x = island.x - 50; x < island.x + 50; x++)
				for(int y = ISLANDS_Y - 35; y < world.getMaxHeight(); y++)
					for(int z = island.z - 50; z < island.z + 50; z++) {
						Block block = world.getBlockAt(x,y,z);
						if(block.getTypeId() != 0)
							block.setTypeId(0);
					}
			orphaned.push(island);
			playerIslands.remove(playerName);
		}
	}
	

	public void registerPlayerIsland(Player player, Island newIsland) {
		playerIslands.put(player.getName(), newIsland);
	}

	public void teleportHome(Player player) {
		Island home = getPlayerIsland(player.getName());
		int h = ISLANDS_Y;
		while(player.getWorld().getBlockTypeIdAt(home.x, h, home.z) != 0) {
			h++;
		}
		player.getWorld().loadChunk(home.x, home.z);
        player.teleport(new Location(player.getWorld(), home.x, h, home.z));	
	};
	
	private void makeSpawn(String worldname) {
		World world = this.getServer().getWorld(worldname);
		if(null == world) {
			System.out.println("No world named \""+ worldname +"\" found, no specific spawn created");
			return;
		}
		Location spawn = world.getSpawnLocation();
		System.out.println("[skySMP] making spawn on skyIsland");
		//make a platform for people to spawn on
		for(int x = (int)(spawn.getX() - 5); x < spawn.getX() + 5; x++)
			for(int z = (int) (spawn.getZ() - 5); z < spawn.getZ() + 5; z++) {	
				Block block = world.getBlockAt(x,spawn.getBlockY(),z);
				block.setTypeId(7);
			}
		
	}
	
	
}


package de.qgel.skySMP;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerEventListener extends PlayerListener {
    private final skySMP plugin;

    public PlayerEventListener(skySMP instance) {
        plugin = instance;
    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
    	if (!plugin.hasIsland(event.getPlayer())) {
    		event.getPlayer().sendMessage("Welcome! This Server uses the Sky Island SMP mod.");
    		event.getPlayer().sendMessage("Use /newIsland to get your very own Island and be teleported there.");
    		event.getPlayer().sendMessage("Use /skyHelp for more commands.");
    		
    		//TP the player to the spawn location, because first join sometimes seems to get fucked up.
    		Location spawn = event.getPlayer().getWorld().getSpawnLocation();
    		event.getPlayer().getWorld().getSpawnLocation().getBlock().getChunk().load();
    		event.getPlayer().teleport(new Location(spawn.getWorld(), spawn.getX(),spawn.getBlockY() + 3, spawn.getZ()));
    	}
    }
    @Override
    public void onPlayerRespawn(PlayerRespawnEvent event) {
    	Player player = event.getPlayer();
    	if(plugin.hasIsland(player)){
    		if(player.getWorld().getEnvironment().getId() == 0) { //if we are  in the normal world
        		Island home = plugin.getPlayerIsland(player.getName());
        		event.setRespawnLocation(new Location(player.getWorld(),home.x,plugin.getISLANDS_Y(),home.z));
    		}

    	}
    }
}
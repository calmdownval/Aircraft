package com.sunkenbridge.val.Aircraft;

import java.util.Arrays;
import java.util.HashSet;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerInteractListener implements Listener {
	
    private final AircraftPlugin plugin;
    
    private static final HashSet<Byte> transparent = new HashSet<Byte>(Arrays.asList(new Byte[] {
			
			0, // air
			8, 9, // water
			10, 11 //lava
	}));

    public PlayerInteractListener(AircraftPlugin plugin) {
    	
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }
    
	@EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
    	
    	Player player = event.getPlayer();
    	
    	try {
    	
    		Aircraft pilot;
    		
    		if (plugin.isQueued(player)) {
    		
    			plugin.unqueue(player);
    			pilot = new Aircraft(
    					player.getTargetBlock(transparent, 4),
    					player,
    					plugin.getSizeLimit());

    			plugin.setAircraft(player, pilot);
    			plugin.sendMessage(player, 
    				AircraftPlugin.LANG_AIRCRAFT_FOUND + " (size: " +
    				pilot.getSize() + ")");
    		}
	    	
	    	else if (event.getItem().getType().equals(plugin.getControlItem())) {
	    	
	    		pilot = plugin.getAircraft(player);
	    		if (pilot != null) {
	    			
	    			pilot.move(Direction.point(
	    				player.getLocation(),
	    				player.getTargetBlock(transparent, 200).getLocation()));
	    		}
	    	}
    	}
    	catch (Exception e) {
    		
    		String msg = e.getMessage();
    		if (msg != null && msg != "")
    			plugin.sendMessage(player, msg);
    	}
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
    	
    	plugin.removeAircraft(event.getPlayer());
    }
}

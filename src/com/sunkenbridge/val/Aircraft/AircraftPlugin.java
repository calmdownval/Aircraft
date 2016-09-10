package com.sunkenbridge.val.Aircraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class AircraftPlugin extends JavaPlugin {
	
	private Material controlItem = Material.WATCH;
	private int sizeLimit = 1000;
	
	private ArrayList<Player> queue;
	private HashMap<Player, Aircraft> pilots;
	
	public static String
		LANG_CREATE_MESSAGE = "Right-click on a block to create an aircraft.",
		LANG_PLAYER_NOT_FOUND = "Player was not found.",
		LANG_PLAYER_HAS_AIRCRAFT = "Player already has an active aircraft.",
		LANG_COPILOT_ADDED = "Player is now your co-pilot.",
		LANG_COPILOT_REMOVED = "Player is no longer your co-pilot.",
		LANG_COPILOT_GOT = "You are now co-pilot with ",
		LANG_COPILOT_NO_LONGER = "You are no longer co-pilot with ",
		LANG_AIRCRAFT_NOT_MATCH = "Player already controlls a different aircraft.",
		LANG_AIRCRAFT_FOUND = "Aircraft created sucessfully, welcome aboard.",
		LANG_NO_ACTIVE_AIRCRAFT = "You don't have any active aircraft.",
		LANG_AIRCRAFT_UNLOADED = "Your current aircraft has been unloaded.",
		LANG_AIRCRAFT_DELETED = "Your current aircraft has been deleted.",
		LANG_AIRCRAFT_NOT_DELETED = "You can't delete this aircraft since you are not the owner.",
		LANG_AIRCRAFT_USE_STOP = "If you wish no longer to be a co-pilot, please use /ac stop.",
		LANG_AIRCRAFT_COL_ON = "Collisions are §aON§e.",
		LANG_AIRCRAFT_COL_OFF = "Collisions are §cOFF§e, be careful!",
		LANG_TELEPORTED_ONBOARD_SELF = "Teleported.",
		LANG_TELEPORTED_ONBOARD = " teleported you aboard this aircraft.",
		LANG_TELEPORTED_LOC = "Your aircraft was teleported to you.",
		LANG_INVALID_NUMBER = "Please enter a valid integer number.",
		LANG_SPEED_SET = "Aircraft speed set.",
		LANG_SPEED_OVERFLOW = "Can't fly faster than 10 blocks per turn.",
		LANG_GROUNDED = "Your aircraft is now at the ground level.",
		LANG_ZERO_SIZE = "No blocks have been selected.",
		LANG_MAX_SIZE = "Maximum size limit exceeded!",
		LANG_MOVE_COL = "Cannot move in this direction, there's a collision.",
		LANG_TELEPORT_COL = "Cannot teleport here, there's a collision.",
		LANG_MUST_BE_OP = "You must be an OP to use aircraft.",
		LANG_WRONG_ARGN = "Wrong number of arguments.",
		LANG_COMMAND_INVALID = "Command was not recognized.";
	
	
	// overriden
    @Override
    public void onEnable() {
    	
    	// save a copy of the default config.yml if one is not there
        saveDefaultConfig();
        
        // load config
        FileConfiguration config = getConfig();
        controlItem = Material.getMaterial(config.getInt("control", 347));
        sizeLimit = config.getInt("max-size", 1000);
        
        // create buffers
        queue  = new ArrayList<Player>();
    	pilots = new HashMap<Player, Aircraft>();
    	
    	// subscribe events & commands
    	new PlayerInteractListener(this);
    	new AircraftCommandExecutor(this);
    }
    
    @Override
    public void onDisable() {
    
    	queue.clear();
    	pilots.clear();
    }
    
    
    // getters & setters
    public Material getControlItem() {
    	
    	return controlItem;
    }
    
    public int getSizeLimit() {
    	
    	return sizeLimit;
    }
    
    
    // queue
    public void enqueue(Player player) {
    	
    	if (!queue.contains(player))
    		queue.add(player);
    }
    
    public void unqueue(Player player) {
    	
    	queue.remove(player);
    }
    
    public boolean isQueued(Player player) {
    	
    	return queue.contains(player);
    }
    
    
    // aircraft
    public Aircraft getAircraft(Player player) {
    
    	return pilots.containsKey(player) ? pilots.get(player) : null;
    }
    
    public void setAircraft(Player player, Aircraft aircraft) {
    	
    	pilots.put(player, aircraft);
    }
    
    public void removeAircraft(Player player) {
    
    	pilots.remove(player);
    }
    
    public boolean hasAircraft(Player player) {
    	
    	return pilots.containsKey(player);
    }
    
    public Player[] getCopilots(Aircraft aircraft) {
    	
    	ArrayList<Player> buffer = new ArrayList<Player>();
    	
    	Set<Player> keys = pilots.keySet();
    	for (Player key : keys)
    		if (pilots.get(key).equals(aircraft))
    			buffer.add(key);
    	
    	return buffer.toArray(new Player[buffer.size()]);
    }
    
    
    // other
    public void sendMessage(Player player, String message) {
    
    	player.sendMessage("§a[Aircraft]:§e " + message);
    }
}